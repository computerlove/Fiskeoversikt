package no.lillehaug.landingsopplysninger.api;

import java.time.LocalDate;
import java.util.Objects;

public class LandingsdataQuery {
    public final LocalDate fraDato;
    public final LocalDate tilDato;

    public LandingsdataQuery(LocalDate fraDato, LocalDate tilDato) {
        this.fraDato = fraDato;
        this.tilDato = tilDato;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LandingsdataQuery that = (LandingsdataQuery) o;
        return Objects.equals(fraDato, that.fraDato) &&
                Objects.equals(tilDato, that.tilDato);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fraDato, tilDato);
    }

    @Override
    public String toString() {
        return "LandingsdataQuery{" + "fraDato=" + fraDato +
                ", tilDato=" + tilDato +
                '}';
    }
}
