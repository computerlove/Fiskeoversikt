package no.lillehaug.landingsopplysninger.scrape

import no.lillehaug.landingsopplysninger.api.LandingsopplysningerScraper
import no.lillehaug.landingsopplysninger.scrape.parse.Parser

class Scraper (url: String) : LandingsopplysningerScraper {
    val url = url

    override fun scrapeForRegistration(registration: String) {
        val parser = Parser(url)

    }

}
