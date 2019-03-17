package no.lillehaug.landingsopplysninger.api

import java.time.LocalDate

data class LandingsdataFraTilQuery(val fraDato: LocalDate?, val tilDato: LocalDate?)
data class LandingsdataNumOffsetQuery(val num: Int, val start: Int)