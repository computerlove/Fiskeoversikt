package no.lillehaug.fiskeoversikt

import no.lillehaug.fiskeoversikt.parse.Parser

class Scraper {
    fun main(args : Array<String>) {
        val url = "http://www.rafisklaget.no/portal/pls/portal/PORTAL.LANDINGSOPPLYSNING.show"
        val parser = Parser(url)
    }


}
