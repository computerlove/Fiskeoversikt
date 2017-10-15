package no.lillehaug.landingsopplysninger.api;

import java.time.LocalDate;
import java.util.UUID;

public class LeveringslinjeWithId extends Leveringslinje {
    public final UUID id;

    public LeveringslinjeWithId(UUID id,
                                String fartøy,
                                LocalDate landingsdato,
                                String mottak,
                                String fiskeslag,
                                String tilstand,
                                String størrelse,
                                String kvalitet,
                                Double nettovekt) {
        super(fartøy, landingsdato, mottak, fiskeslag, tilstand, størrelse, kvalitet, nettovekt);
        this.id = id;
    }

    public Leveringslinje withoutId() {
        return new Leveringslinje(fartøy, landingsdato, mottak, fiskeslag, tilstand, størrelse, kvalitet, nettovekt);
    }
}
