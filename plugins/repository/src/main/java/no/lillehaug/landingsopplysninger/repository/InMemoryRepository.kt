package no.lillehaug.landingsopplysninger.repository

import no.lillehaug.landingsopplysninger.api.LandingsopplysningerRepository
import no.lillehaug.landingsopplysninger.api.Leveringslinje

class InMemoryRepository : LandingsopplysningerRepository {
    val leveranselinjer = mutableListOf<Leveringslinje>()

    override fun lagreLeveranselinjer(leveranselinjer: Collection<Leveringslinje>) {
        this.leveranselinjer.addAll(leveranselinjer)
    }

    override fun alleLeveranselinjer(): List<Leveringslinje> {
        return this.leveranselinjer
    }
}
