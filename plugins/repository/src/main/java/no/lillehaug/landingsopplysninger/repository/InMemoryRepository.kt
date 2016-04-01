package no.lillehaug.landingsopplysninger.repository

import no.lillehaug.landingsopplysninger.api.LandingsopplysningerRepository
import no.lillehaug.landingsopplysninger.api.Leveringslinje
import java.time.LocalDate

class InMemoryRepository : LandingsopplysningerRepository {
    override fun forrigeLandingFor(registration: String): LocalDate {
        val max = leveranselinjer.map { it.landingsdato }.max()
        return max ?: LocalDate.MIN
    }

    val leveranselinjer = mutableListOf<Leveringslinje>()

    override fun lagreLeveranselinjer(leveranselinjer: Collection<Leveringslinje>) {
        this.leveranselinjer.addAll(leveranselinjer)
    }

    override fun alleLeveranselinjer(): List<Leveringslinje> {
        return this.leveranselinjer
    }
}
