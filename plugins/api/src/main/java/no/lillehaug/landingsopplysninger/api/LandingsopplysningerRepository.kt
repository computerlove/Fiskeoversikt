package no.lillehaug.landingsopplysninger.api

interface LandingsopplysningerRepository {
    fun alleLeveranselinjer() : List<Leveringslinje>
    fun lagreLeveranselinjer(leveranselinjer: Collection<Leveringslinje>)
}
