package no.lillehaug.landingsopplysninger.repository.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;
import reactor.core.publisher.MonoSink;
import reactor.util.context.Context;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.function.BiConsumer;
import java.util.function.LongConsumer;

public class AsyncJDBC {

    private static final Logger logger = LoggerFactory.getLogger(AsyncJDBC.class);

    public static <T> Flux<T> readonlyFlux(DataSource ds, BiConsumer<Connection, FluxSink<T>> consumer)  {
        return Flux.create(consumerF -> {
            ConnectionFluxSink<T> sink = ConnectionFluxSink.create(ds, consumerF, true, false);
            consumer.accept(sink.connection, sink);
        });
    }

    public static <T> Flux<T> transactionalFlux(DataSource ds, BiConsumer<Connection, FluxSink<T>> consumer) {
        return Flux.create(consumerF -> {
            ConnectionFluxSink<T> sink = ConnectionFluxSink.create(ds, consumerF, false, false);
            consumer.accept(sink.connection, sink);
        });
    }

    public static <T> Mono<T> transactionalMono(DataSource ds, BiConsumer<Connection, MonoSink<T>> consumer)  {
        return Mono.create(consumerF -> {
            ConnectionMonoSink<T> sink = ConnectionMonoSink.create(ds, consumerF, false, false);
            consumer.accept(sink.connection, sink);
        });
    }

    public static <T> Mono<T> readonlyMono(DataSource ds, BiConsumer<Connection, MonoSink<T>> consumer)  {
        return Mono.create(consumerF -> {
            ConnectionMonoSink<T> sink = ConnectionMonoSink.create(ds, consumerF, true, false);
            consumer.accept(sink.connection, sink);
        });
    }

    private static class ConnectionFluxSink<T> implements FluxSink<T> {
        final Connection connection;
        private final boolean readonly;
        private final boolean autoCommit;
        private final FluxSink<T> consumer;

        ConnectionFluxSink(Connection connection,
                                  boolean readonly,
                                  boolean autoCommit,
                                  FluxSink<T> consumerF) {
            this.connection = connection;
            this.readonly = readonly;
            this.autoCommit = autoCommit;
            consumer = consumerF;
        }

        static <T> ConnectionFluxSink<T> create(DataSource ds,
                                                       FluxSink<T> sink,
                                                       boolean useReadonly,
                                                       boolean useAutoCommit) {
            try {
                Connection c = ds.getConnection();
                boolean readOnly = c.isReadOnly();
                boolean autoCommit = c.getAutoCommit();
                c.setReadOnly(useReadonly);
                c.setAutoCommit(useAutoCommit);
                return new ConnectionFluxSink<T>(c, readOnly, autoCommit, sink);
            } catch (SQLException e) {
                logger.error("Error getting connection", e);
                sink.error(e);
                return new ConnectionFluxSink<>(null, false, false, sink);
            }
        }

        @Override
        public void complete() {
            if(this.connection == null) {
                RuntimeException e = new RuntimeException("Complete with non existing connection");
                logger.error("Complete with non existing connection", e);
                consumer.error(e);
            } else {
                if(!this.autoCommit) {
                    try {
                        this.connection.commit();
                    } catch (SQLException e) {
                        logger.error("Error commiting", e);
                        consumer.error(e);
                    }
                }
                try {
                    this.connection.setAutoCommit(this.autoCommit);
                    this.connection.setReadOnly(this.readonly);
                } catch (SQLException e) {
                    logger.error("Error resetting state", e);
                    consumer.error(e);
                }

                try {
                    this.connection.close();
                } catch (SQLException e) {
                    logger.error("Error closing", e);
                    consumer.error(e);
                }
            }
            consumer.complete();
        }

        @Override
        public Context currentContext() {
            return consumer.currentContext();
        }

        @Override
        public void error(Throwable e) {
            // TODO close?
            consumer.error(e);
        }

        @Override
        public FluxSink<T> next(T t) {
            if(this.connection == null) {
                RuntimeException e = new RuntimeException("Next with non existing connection");
                logger.error("Next with non existing connection", e);
                consumer.error(e);
            }
            return consumer.next(t);
        }

        @Override
        public long requestedFromDownstream() {
            return consumer.requestedFromDownstream();
        }

        @Override
        public boolean isCancelled() {
            return consumer.isCancelled();
        }

        @Override
        public FluxSink<T> onRequest(LongConsumer consumer) {
            return this.consumer.onRequest(consumer);
        }

        @Override
        public FluxSink<T> onCancel(Disposable d) {
            // TODO close?
            return consumer.onCancel(d);
        }

        @Override
        public FluxSink<T> onDispose(Disposable d) {
            // TODO close?
            return consumer.onDispose(d);
        }
    }

    private static class ConnectionMonoSink<T> implements MonoSink<T> {
        private final Connection connection;
        private final boolean readOnly;
        private final boolean autoCommit;
        private final MonoSink<T> sink;

        public ConnectionMonoSink(Connection c, boolean readOnly, boolean autoCommit, MonoSink<T> sink) {
            this.connection = c;
            this.readOnly = readOnly;
            this.autoCommit = autoCommit;
            this.sink = sink;
        }

        @Override
        public Context currentContext() {
            return sink.currentContext();
        }

        @Override
        public void success() {
            sink.success();
        }

        private void close() {
            if(this.connection == null) {
                RuntimeException e = new RuntimeException("Complete with non existing connection");
                logger.error("Complete with non existing connection", e);
                sink.error(e);
            } else {
                if(!this.autoCommit) {
                    try {
                        this.connection.commit();
                    } catch (SQLException e) {
                        logger.error("Error commiting", e);
                        sink.error(e);
                    }
                }
                try {
                    this.connection.setAutoCommit(this.autoCommit);
                    this.connection.setReadOnly(this.readOnly);
                } catch (SQLException e) {
                    logger.error("Error resetting state", e);
                    sink.error(e);
                }

                try {
                    this.connection.close();
                } catch (SQLException e) {
                    logger.error("Error closing", e);
                    sink.error(e);
                }
            }
        }

        @Override
        public void success(T value) {
            close();
            sink.success(value);
        }

        @Override
        public void error(Throwable e) {
            // TODO close?
            sink.error(e);
        }

        @Override
        public MonoSink<T> onRequest(LongConsumer consumer) {
            return sink.onRequest(consumer);
        }

        @Override
        public MonoSink<T> onCancel(Disposable d) {
            // TODO close?
            return sink.onCancel(d);
        }

        @Override
        public MonoSink<T> onDispose(Disposable d) {
            // TODO close?
            return sink.onDispose(d);
        }

        static <T> ConnectionMonoSink<T> create(DataSource ds,
                                                       MonoSink<T> sink,
                                                       boolean useReadonly,
                                                       boolean useAutoCommit) {
            try {
                Connection c = ds.getConnection();
                boolean readOnly = c.isReadOnly();
                boolean autoCommit = c.getAutoCommit();
                c.setReadOnly(useReadonly);
                c.setAutoCommit(useAutoCommit);
                return new ConnectionMonoSink<>(c, readOnly, autoCommit, sink);
            } catch (SQLException e) {
                logger.error("Error getting connection", e);
                sink.error(e);
                return new ConnectionMonoSink<>(null, false, false, sink);
            }
        }
    }
}

