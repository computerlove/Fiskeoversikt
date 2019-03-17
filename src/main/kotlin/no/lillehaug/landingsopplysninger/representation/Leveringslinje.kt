package no.lillehaug.landingsopplysninger.representation

import java.util.*

data class Leveringslinje (val id: UUID, val fartoy: String, val landingsdato: String, val mottak: String, val fiskeslag: String, val tilstand: String, val storrelse: String, val kvalitet: String, val nettovekt: Double) {
    companion object {
        @JvmStatic
        fun fromApi(l: no.lillehaug.landingsopplysninger.api.LeveringslinjeWithId) : Leveringslinje {
            return Leveringslinje(l.id, l.fartøy, l.landingsdato.toString(), l.mottak, l.fiskeslag, l.tilstand, l.størrelse, l.kvalitet, l.nettovekt)
        }
    }
}
