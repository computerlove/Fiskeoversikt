package no.lillehaug.fiskeoversikt

import java.time.LocalDate

data class Leveringslinje (val fartøy: String, val landingsdato: LocalDate, val mottak: String, val fiskeslag: String, val tilstand: String, val størrelse: String, val kvalitet: String, val nettovekt: Double)
