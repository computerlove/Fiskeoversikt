package no.lillehaug.landingsopplysninger.repository

import no.lillehaug.landingsopplysninger.api.LandingsopplysningerRepository
import no.lillehaug.landingsopplysninger.api.Leveringslinje

class InMemoryRepository : LandingsopplysningerRepository {
    override fun alleLeveranselinjer(): List<Leveringslinje> {
        throw UnsupportedOperationException()
    }
}
