package no.lillehaug.landingsopplysninger.api

import java.time.LocalDate

interface LandingsopplysningerRepository {
    fun alleLeveranselinjer(): List<LeveringslinjeWithId>
    fun alleLeveranselinjer(landingsdataQuery: LandingsdataQuery): List<LeveringslinjeWithId>
    fun lagreLeveranselinjer(leveranselinjer: Collection<Leveringslinje>)

    fun forrigeLandingFor(registration: String): LocalDate
}
