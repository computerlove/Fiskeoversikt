package no.lillehaug.landingsopplysninger.api

interface LandingsopplysningerRepository {
    fun alleLeveranselinjer() : List<Leveringslinje>
}
