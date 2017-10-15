package no.lillehaug.landingsopplysninger.repository;

import no.lillehaug.landingsopplysninger.api.LandingsdataQuery;
import no.lillehaug.landingsopplysninger.api.LandingsopplysningerRepository;
import no.lillehaug.landingsopplysninger.api.Leveringslinje;
import no.lillehaug.landingsopplysninger.api.LeveringslinjeWithId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

class JDBCRepositoryTest {
    private final Database database = Database.createForTest();
    private final LandingsopplysningerRepository repository = new JDBCRepository(database);

    @BeforeEach
    void setUp() {
        database.clean();
        database.migrate();
    }

    @Test
    void insertAndGetAll() {
        Flux<LeveringslinjeWithId> alleLinjer = repository.lagreLeveranselinjer(Flux.fromIterable(TEST_DATA));
        Set<Leveringslinje> leveringslinjer = alleLinjer.toStream()
                                                        .map(LeveringslinjeWithId::withoutId)
                                                        .collect(Collectors.toSet());

        assertIterableEquals(new HashSet<>(TEST_DATA), leveringslinjer, "Saved and retrieved did not equal test data");
    }

    @Test
    void getMaksLandinsdato() {
        Flux<LeveringslinjeWithId> alleLinjer = repository.lagreLeveranselinjer(Flux.fromIterable(TEST_DATA));
        LocalDate maks = alleLinjer.toStream()
                                   .map(leveringslinjeWithId -> leveringslinjeWithId.landingsdato)
                                   .max(LocalDate::compareTo)
                                   .get();
        StepVerifier.create(repository.forrigeLandingFor("N 0027SG"))
                .expectNext(maks)
                .verifyComplete();
    }

    @Test
    void tilDato() {
        repository.lagreLeveranselinjer(Flux.fromIterable(TEST_DATA)).blockLast();

        LocalDate til = LocalDate.of(2016, 3, 20);
        Flux<LeveringslinjeWithId> linjer = repository.alleLeveranselinjer(new LandingsdataQuery(null, til));
        LocalDate localDate = linjer.toStream().map(l -> l.landingsdato).max(LocalDate::compareTo).get();
        assertEquals(til, localDate, "latest date was not as expected");
    }

    @Test
    void fraDato() {
        repository.lagreLeveranselinjer(Flux.fromIterable(TEST_DATA)).blockLast();

        LocalDate fra = LocalDate.of(2016, 3, 19);
        Flux<LeveringslinjeWithId> linjer = repository.alleLeveranselinjer(new LandingsdataQuery(fra, null));
        LocalDate localDate = linjer.toStream().map(l -> l.landingsdato).min(LocalDate::compareTo).get();
        assertEquals(fra, localDate, "date was not as expected");
    }

    @Test
    void fraOgTilDate() {
        repository.lagreLeveranselinjer(Flux.fromIterable(TEST_DATA)).blockLast();

        LocalDate fra = LocalDate.of(2016, 3, 19);
        LocalDate til = LocalDate.of(2016, 3, 22);
        Flux<LeveringslinjeWithId> leveringslinjer = repository.alleLeveranselinjer(new LandingsdataQuery(fra, til));
        LocalDate min = leveringslinjer.toStream().map(l -> l.landingsdato).min(LocalDate::compareTo).get();
        LocalDate max = leveringslinjer.toStream().map(l -> l.landingsdato).max(LocalDate::compareTo).get();
        assertEquals(fra, min);
        assertEquals(til, max);
    }

    private static final List<Leveringslinje> TEST_DATA = Arrays.asList(
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 29), "Steigen Sjømat As (N869)", "Hyse", "Sluh", "-0,8 Kg", "A", 1.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 29), "Steigen Sjømat As (N869)", "Kveite", "Sluh", "-20 Kg", "A", 6.5),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 29), "Steigen Sjømat As (N869)", "Kvitlange", "Sluh", "2,0+ Kg", "A", 26.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 29), "Steigen Sjømat As (N869)", "Lyr", "Sluh", "2,0+ Kg", "A", 12.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 29), "Steigen Sjømat As (N869)", "Sei", "Sluh", "2,3+ Kg", "A", 3.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 29), "Steigen Sjømat As (N869)", "Torsk", "Sluh", "6,0+ Kg", "A", 539.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 29), "Steigen Sjømat As (N869)", "Torsk", "Sluh", "2,5+ Kg", "A", 1049.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 28), "Steigen Sjømat As (N869)", "Kvitlange", "Sluh", "2,0+ Kg", "A", 20.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 28), "Steigen Sjømat As (N869)", "Lyr", "Sluh", "2,0+ Kg", "A", 11.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 28), "Steigen Sjømat As (N869)", "Sei", "Sluh", "1,2-2,3 Kg", "A", 4.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 28), "Steigen Sjømat As (N869)", "Torsk", "Sluh", "6,0+ Kg", "A", 678.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 28), "Steigen Sjømat As (N869)", "Torsk", "Sluh", "2,5+ Kg", "Skadd", 50.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 28), "Steigen Sjømat As (N869)", "Torsk", "Sluh", "2,5+ Kg", "A", 1545.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 22), "Steigen Sjømat As (N869)", "Gråstbit", "Sluh", "1,0+ Kg", "A", 4.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 22), "Steigen Sjømat As (N869)", "Hyse", "Sluh", "-0,8 Kg", "A", 6.5),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 22), "Steigen Sjømat As (N869)", "Lyr", "Sluh", "2,0+ Kg", "A", 3.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 22), "Steigen Sjømat As (N869)", "Sei", "Sluh", "2,3+ Kg", "A", 5.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 22), "Steigen Sjømat As (N869)", "Sei", "Sluh", "1,2-2,3 Kg", "A", 5.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 22), "Steigen Sjømat As (N869)", "Torsk", "Sluh", "2,5+ Kg", "A", 260.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 22), "Steigen Sjømat As (N869)", "Torsk", "Sluh", "6,0+ Kg", "A", 225.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 22), "Steigen Sjømat As (N869)", "Torsk", "Sluh", "1,0-2,5 Kg", "A", 10.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 22), "Steigen Sjømat As (N869)", "Torsk", "Rogn", "Unspec", "Skadd", 60.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 21), "Steigen Sjømat As (N869)", "Hyse", "Sluh", "0,8+ Kg", "A", 8.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 21), "Steigen Sjømat As (N869)", "Kvitlange", "Sluh", "2,0+ Kg", "A", 2.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 21), "Steigen Sjømat As (N869)", "Sei", "Sluh", "2,3+ Kg", "A", 10.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 21), "Steigen Sjømat As (N869)", "Torsk", "Sluh", "1,0-2,5 Kg", "A", 20.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 21), "Steigen Sjømat As (N869)", "Torsk", "Sluh", "2,5+ Kg", "A", 361.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 21), "Steigen Sjømat As (N869)", "Torsk", "Rogn", "Unspec", "A", 64.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 21), "Steigen Sjømat As (N869)", "Torsk", "Sluh", "6,0+ Kg", "A", 216.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 20), "Steigen Sjømat As (N869)", "Hyse", "Sluh", "0,8+ Kg", "A", 3.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 20), "Steigen Sjømat As (N869)", "Kveite", "Sluh", "-20 Kg", "A", 6.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 20), "Steigen Sjømat As (N869)", "Kvitlange", "Sluh", "2,0+ Kg", "A", 3.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 20), "Steigen Sjømat As (N869)", "Sei", "Sluh", "1,2-2,3 Kg", "A", 15.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 20), "Steigen Sjømat As (N869)", "Sei", "Sluh", "2,3+ Kg", "A", 20.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 20), "Steigen Sjømat As (N869)", "Torsk", "Sluh", "2,5+ Kg", "A", 600.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 20), "Steigen Sjømat As (N869)", "Torsk", "Rogn", "Unspec", "A", 89.5),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 20), "Steigen Sjømat As (N869)", "Torsk", "Sluh", "6,0+ Kg", "A", 393.5),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 20), "Steigen Sjømat As (N869)", "Torsk", "Sluh", "1,0-2,5 Kg", "A", 24.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 20), "Steigen Sjømat As (N869)", "Torsk", "Sluh", "2,5+ Kg", "Skadd", 5.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 19), "Steigen Sjømat As (N869)", "Hyse", "Sluh", "-0,8 Kg", "A", 8.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 19), "Steigen Sjømat As (N869)", "Kveite", "Sluh", "-20 Kg", "A", 13.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 19), "Steigen Sjømat As (N869)", "Kvitlange", "Sluh", "2,0+ Kg", "A", 10.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 19), "Steigen Sjømat As (N869)", "Sei", "Sluh", "1,2-2,3 Kg", "A", 8.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 19), "Steigen Sjømat As (N869)", "Sei", "Sluh", "2,3+ Kg", "A", 25.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 19), "Steigen Sjømat As (N869)", "Torsk", "Rogn", "Unspec", "Skadd", 45.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 19), "Steigen Sjømat As (N869)", "Torsk", "Sluh", "6,0+ Kg", "A", 214.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 19), "Steigen Sjømat As (N869)", "Torsk", "Sluh", "1,0-2,5 Kg", "A", 24.5),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 19), "Steigen Sjømat As (N869)", "Torsk", "Sluh", "2,5+ Kg", "A", 280.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 18), "Steigen Sjømat As (N869)", "Brosme", "Sluh", "1,0-2,0 Kg", "A", 3.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 18), "Steigen Sjømat As (N869)", "Hyse", "Sluh", "-0,8 Kg", "A", 4.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 18), "Steigen Sjømat As (N869)", "Torsk", "Sluh", "6,0+ Kg", "A", 73.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 18), "Steigen Sjømat As (N869)", "Torsk", "Sluh", "2,5+ Kg", "A", 173.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 18), "Steigen Sjømat As (N869)", "Torsk", "Rogn", "Unspec", "Skadd", 27.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 17), "Steigen Sjømat As (N869)", "Hyse", "Sluh", "0,8+ Kg", "A", 6.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 17), "Steigen Sjømat As (N869)", "Hyse", "Sluh", "-0,8 Kg", "A", 3.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 17), "Steigen Sjømat As (N869)", "Kveite", "Sluh", "-20 Kg", "A", 14.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 17), "Steigen Sjømat As (N869)", "Kvitlange", "Sluh", "0,7-2 Kg", "A", 1.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 17), "Steigen Sjømat As (N869)", "Sei", "Sluh", "2,3+ Kg", "A", 10.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 17), "Steigen Sjømat As (N869)", "Torsk", "Rogn", "Unspec", "A", 27.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 17), "Steigen Sjømat As (N869)", "Torsk", "Sluh", "2,5+ Kg", "A", 275.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 17), "Steigen Sjømat As (N869)", "Torsk", "Sluh", "6,0+ Kg", "A", 153.0)
    );
}
