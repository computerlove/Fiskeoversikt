package no.lillehaug.landingsopplysninger.api;


import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public class Leveringslinje {
    public final String fartøy;
    public final LocalDate landingsdato;
    public final String mottak;
    public final String fiskeslag;
    public final String tilstand;
    public final String størrelse;
    public final String kvalitet;
    public final Double nettovekt;

    public Leveringslinje(String fartøy,
                          LocalDate landingsdato,
                          String mottak,
                          String fiskeslag,
                          String tilstand,
                          String størrelse,
                          String kvalitet,
                          Double nettovekt) {
        this.fartøy = fartøy;
        this.landingsdato = landingsdato;
        this.mottak = mottak;
        this.fiskeslag = fiskeslag;
        this.tilstand = tilstand;
        this.størrelse = størrelse;
        this.kvalitet = kvalitet;
        this.nettovekt = nettovekt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Leveringslinje that = (Leveringslinje) o;
        return Objects.equals(fartøy, that.fartøy) &&
                Objects.equals(landingsdato, that.landingsdato) &&
                Objects.equals(mottak, that.mottak) &&
                Objects.equals(fiskeslag, that.fiskeslag) &&
                Objects.equals(tilstand, that.tilstand) &&
                Objects.equals(størrelse, that.størrelse) &&
                Objects.equals(kvalitet, that.kvalitet) &&
                Objects.equals(nettovekt, that.nettovekt);
    }

    public LeveringslinjeWithId withId(UUID id) {
        return new LeveringslinjeWithId(id, fartøy, landingsdato, mottak,
                                        fiskeslag, tilstand, størrelse, kvalitet, nettovekt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fartøy, landingsdato, mottak, fiskeslag, tilstand, størrelse, kvalitet, nettovekt);
    }

    @Override
    public String toString() {
        return "Leveringslinje{" + "fartøy='" + fartøy + '\'' +
                ", landingsdato=" + landingsdato +
                ", mottak='" + mottak + '\'' +
                ", fiskeslag='" + fiskeslag + '\'' +
                ", tilstand='" + tilstand + '\'' +
                ", størrelse='" + størrelse + '\'' +
                ", kvalitet='" + kvalitet + '\'' +
                ", nettovekt=" + nettovekt +
                '}';
    }
}
