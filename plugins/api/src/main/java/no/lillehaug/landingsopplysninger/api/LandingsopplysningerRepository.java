package no.lillehaug.landingsopplysninger.api;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

public interface LandingsopplysningerRepository {
    Flux<LeveringslinjeWithId> alleLeveranselinjer();
    Flux<LeveringslinjeWithId> alleLeveranselinjer(LandingsdataQuery query);
    Flux<LeveringslinjeWithId> lagreLeveranselinjer(Flux<Leveringslinje> leveringslinjer);

    Mono<LocalDate> forrigeLandingFor(String registration);
}
