package no.lillehaug.landingsopplysninger.scrape

import no.lillehaug.landingsopplysninger.api.Leveringslinje
import no.lillehaug.landingsopplysninger.scrape.parse.Parser

class Scraper (val url: String) {

    fun scrapeForRegistration(registration: String) : List<Leveringslinje> {
        val parser = Parser(url)
        return parser.fetchAndParseForRegistration(registration)
    }

}
