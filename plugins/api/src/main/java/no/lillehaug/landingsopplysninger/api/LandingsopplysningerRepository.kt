package no.lillehaug.landingsopplysninger.api

import java.time.LocalDate

interface LandingsopplysningerRepository {
    fun alleLeveranselinjer() : List<Leveringslinje>
    fun lagreLeveranselinjer(leveranselinjer: Collection<Leveringslinje>)

    fun forrigeLandingFor(registration: String): LocalDate
}
