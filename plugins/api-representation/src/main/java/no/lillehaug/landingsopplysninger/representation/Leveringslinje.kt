package no.lillehaug.landingsopplysninger.representation

data class Leveringslinje (val fartoy: String, val landingsdato: String, val mottak: String, val fiskeslag: String, val tilstand: String, val storrelse: String, val kvalitet: String, val nettovekt: Double) {
    companion object {
        @JvmStatic
        fun fromApi(l: no.lillehaug.landingsopplysninger.api.Leveringslinje) : Leveringslinje {
            return Leveringslinje(l.fartøy, l.landingsdato.toString(), l.mottak, l.fiskeslag, l.tilstand, l.størrelse, l.kvalitet, l.nettovekt)
        }
    }
}
