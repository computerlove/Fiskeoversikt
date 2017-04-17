package no.lillehaug.landingsopplysninger.api

import java.time.LocalDate

interface LandingsopplysningerRepository {
    fun alleLeveranselinjer(): List<LeveringslinjeWithId>
    fun alleLeveranselinjer(landingsdataQuery: LandingsdataFraTilQuery): List<LeveringslinjeWithId>
    fun alleLeveranselinjerByDates(num: Int, start: Int): List<LeveringslinjeWithId>

    fun lagreLeveranselinjer(leveranselinjer: Collection<Leveringslinje>)

    fun forrigeLandingFor(registration: String): LocalDate

}
