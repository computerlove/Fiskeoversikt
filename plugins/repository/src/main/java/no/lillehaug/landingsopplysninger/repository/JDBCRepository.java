package no.lillehaug.landingsopplysninger.repository;

import no.lillehaug.landingsopplysninger.api.LandingsdataQuery;
import no.lillehaug.landingsopplysninger.api.LandingsopplysningerRepository;
import no.lillehaug.landingsopplysninger.api.Leveringslinje;
import no.lillehaug.landingsopplysninger.api.LeveringslinjeWithId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;
import reactor.core.publisher.MonoSink;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JDBCRepository implements LandingsopplysningerRepository {
    private static final Logger logger = LoggerFactory.getLogger(JDBCRepository.class);
    private final Database database;
    private final Scheduler single = Schedulers.single();

    public JDBCRepository(Database database) {
        this.database = database;
    }

    @Override
    public Flux<LeveringslinjeWithId> alleLeveranselinjer() {
        return alleLeveranselinjer(new LandingsdataQuery(null, null));
    }

    @Override
    public Flux<LeveringslinjeWithId> alleLeveranselinjer(LandingsdataQuery query) {
        Flux<LeveringslinjeWithId> leveringslinjeWithIdFlux = database.asyncReadonlyFlux((Connection c, FluxSink<LeveringslinjeWithId> sink) -> {
            try (PreparedStatement ps = c.prepareStatement("select * from leveringslinje " + getWhere(query) + " order by landingsdato desc,fiskeslag,kvalitet")) {
                List<Object> params = getParams(query);
                int i = 1;
                for (Object param : params) {
                    ps.setObject(i++, param);
                }
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        sink.next(
                                new LeveringslinjeWithId(
                                        rs.getObject("id", UUID.class),
                                        rs.getString("fartøy"),
                                        rs.getDate("landingsdato").toLocalDate(),
                                        rs.getString("mottak"),
                                        rs.getString("fiskeslag"),
                                        rs.getString("tilstand"),
                                        rs.getString("størrelse"),
                                        rs.getString("kvalitet"),
                                        rs.getDouble("nettovekt"))
                        );
                    }
                }
            } catch (SQLException e) {
                sink.error(e);
            } finally {
                sink.complete();
            }
        });
        leveringslinjeWithIdFlux.publishOn(single);
        return leveringslinjeWithIdFlux;
    }

    @Override
    public Flux<LeveringslinjeWithId> lagreLeveranselinjer(Flux<Leveringslinje> leveringslinjer) {
        leveringslinjer.publishOn(single);
        return database.asyncTransactionalFlux((Connection c, FluxSink<LeveringslinjeWithId> sink) -> {
            try(PreparedStatement ps = c.prepareStatement("insert into leveringslinje(id, fartøy, landingsdato, mottak, fiskeslag, tilstand, størrelse, kvalitet, nettovekt) values (?,?,?,?,?,?,?,?,?)")) {
                leveringslinjer.subscribe(l -> {
                    try {
                        UUID uuid = UUID.randomUUID();
                        ps.setObject(1, uuid);
                        ps.setString(2, l.fartøy);
                        ps.setDate(3, Date.valueOf(l.landingsdato));
                        ps.setString(4, l.mottak);
                        ps.setString(5, l.fiskeslag);
                        ps.setString(6, l.tilstand);
                        ps.setString(7, l.størrelse);
                        ps.setString(8, l.kvalitet);
                        ps.setDouble(9, l.nettovekt);
                        ps.executeUpdate();

                        LeveringslinjeWithId t = l.withId(uuid);
                        logger.info("Lagrer {}", l);
                        sink.next(t);
                    } catch (SQLException e) {
                        sink.error(e);
                        throw new RuntimeException(e);
                    }
                });
            } catch (SQLException e) {
                sink.error(e);
            } finally {
                sink.complete();
            }
        });
    }

    @Override
    public Mono<LocalDate> forrigeLandingFor(String registration) {
        return database.asyncReadonlyMono((Connection c, MonoSink<LocalDate> sink) -> {
            try( PreparedStatement ps = c.prepareStatement("select max(landingsdato) as landingsdato from leveringslinje where fartøy = ?") ){
                ps.setString(1, registration);
                try (ResultSet rs = ps.executeQuery()) {
                    if( rs.next() ) {
                        Date landingsdato = rs.getDate("landingsdato");
                        sink.success(landingsdato != null ? landingsdato.toLocalDate() : LocalDate.MIN);
                    } else {
                        sink.success(LocalDate.MIN);
                    }
                }
            } catch (SQLException e) {
                sink.error(e);
            } finally {
                sink.success();
            }
        });
    }

    private static String getWhere(LandingsdataQuery query) {
        if(query.tilDato == null && query.fraDato == null) {
            return "";
        }
        Stream.Builder<String> builder = Stream.<String>builder();
        if(query.fraDato != null) {
            builder.add(" landingsdato >= ?");
        }
        if(query.tilDato != null) {
            builder.add(" landingsdato <= ?");
        }

        return builder.build().collect(Collectors.joining(" AND ", "WHERE ", ""));
    }

    private static List<Object> getParams(LandingsdataQuery query) {
        List<Object> params = new ArrayList<>(2);

        if(query.fraDato != null) {
            params.add(Date.valueOf(query.fraDato));
        }
        if(query.tilDato != null) {
            params.add(Date.valueOf(query.tilDato));
        }
        return params;
    }

}
