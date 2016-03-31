package no.lillehaug.landingsopplysninger.representation

import no.lillehaug.landingsopplysninger.api.Leveringslinje
import java.time.LocalDate

data class Leveringslinje (val fartøy: String, val landingsdato: LocalDate, val mottak: String, val fiskeslag: String, val tilstand: String, val størrelse: String, val kvalitet: String, val nettovekt: Double) {
    companion object Convert {
        fun fromApi(l: no.lillehaug.landingsopplysninger.api.Leveringslinje) : Leveringslinje {
            return no.lillehaug.landingsopplysninger.api.Leveringslinje(l.fartøy, l.landingsdato, l.mottak, l.fiskeslag, l.tilstand, l.størrelse, l.kvalitet, l.nettovekt)
        }
    }
}
