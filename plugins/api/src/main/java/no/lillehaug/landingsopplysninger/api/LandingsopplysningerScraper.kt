package no.lillehaug.landingsopplysninger.api

interface LandingsopplysningerScraper {
    fun scrapeForRegistration(registration: String)
}
