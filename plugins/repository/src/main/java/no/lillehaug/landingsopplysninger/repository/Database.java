package no.lillehaug.landingsopplysninger.repository;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.health.HealthCheckRegistry;
import no.lillehaug.landingsopplysninger.repository.jdbc.AsyncJDBC;
import org.flywaydb.core.Flyway;
import org.h2.jdbcx.JdbcConnectionPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;
import reactor.core.publisher.MonoSink;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;

public class Database  {
    private static final Logger logger = LoggerFactory.getLogger(Database.class);

    private final Flyway flyway;
    private final DataSource dataSource;
    private final Scheduler scheduler;

    private Database(DataSource dataSource, Scheduler scheduler) {
        this.dataSource = dataSource;
        this.scheduler = scheduler;
        this.flyway = new Flyway();
        flyway.setDataSource(dataSource);
        flyway.setLocations("db/schema");

    }

    public void migrate() {
        flyway.migrate();
    }

    public void shutdown() {
        logger.debug("Shutdown called");
        if(dataSource instanceof JdbcConnectionPool) {
            ((JdbcConnectionPool)dataSource).dispose();
        }
        this.scheduler.dispose();
        logger.info("Database shutdown");
    }

    public void clean() {
        flyway.clean();
    }

    public static Database create(String url,
                                  String username,
                                  String password,
                                  int connectionPoolSize,
                                  HealthCheckRegistry healthCheckRegistry,
                                  MetricRegistry metricRegistry) {
        int actuallConnectionPoolSize = Math.max(10, connectionPoolSize);
        DataSource dataSource = createDataSource(url, username, password, actuallConnectionPoolSize);

        return new Database(dataSource, Schedulers.fromExecutor(Executors.newFixedThreadPool(actuallConnectionPoolSize)));
    }

    public static Database createForTest() {
        return create("jdbc:h2:mem:test",
                "sa",
                "",
                2,
                null,
                null);
    }

    private static DataSource createDataSource(String url, String username, String password, int connectionPoolSize) {
        logger.debug("Creating datasource with url {} and username {} and pool size {}", url, username, connectionPoolSize);

        JdbcConnectionPool pool = JdbcConnectionPool.create(url, username, password);
        pool.setMaxConnections(connectionPoolSize);
        return pool;
    }


/*
    public <T> T readonly(Function<Connection, T> action) throws SQLException {
        return JDBC.readOnly(this.dataSource, action);
    }

    public <T> T transactional(Function<Connection, T> action) throws SQLException {
        return JDBC.readOnly(this.dataSource, action);
    }
*/

    public <T> Flux<T> asyncReadonlyFlux(BiConsumer<Connection, FluxSink<T>> consumer) {
        return AsyncJDBC.readonlyFlux(this.dataSource, consumer)
                        .publishOn(scheduler);
    }

    public <T> Mono<T> asyncReadonlyMono(BiConsumer<Connection, MonoSink<T>> consumer) {
        return AsyncJDBC.readonlyMono(this.dataSource, consumer)
                        .publishOn(scheduler);
    }

    public <T> Flux<T> asyncTransactionalFlux(BiConsumer<Connection, FluxSink<T>> consumer) {
        return AsyncJDBC.transactionalFlux(this.dataSource, consumer)
                .publishOn(scheduler);
    }
}
