package no.lillehaug.landingsopplysninger.api

import java.time.LocalDate
import java.util.*

data class LeveringslinjeWithId (val id: UUID, val fartøy: String, val landingsdato: LocalDate, val mottak: String, val fiskeslag: String, val tilstand: String, val størrelse: String, val kvalitet: String, val nettovekt: Double)
data class Leveringslinje (val fartøy: String, val landingsdato: LocalDate, val mottak: String, val fiskeslag: String, val tilstand: String, val størrelse: String, val kvalitet: String, val nettovekt: Double)
