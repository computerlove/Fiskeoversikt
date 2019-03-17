package no.lillehaug.landingsopplysninger.scrape

import no.lillehaug.landingsopplysninger.api.Leveringslinje
import no.lillehaug.landingsopplysninger.scrape.parse.Parser
import org.apache.http.impl.client.CloseableHttpClient

class Scraper (val url: String) {

    fun scrapeForRegistration(registration: String, httpclient: CloseableHttpClient) : List<Leveringslinje> {
        val parser = Parser(url)
        return parser.fetchAndParseForRegistration(registration, httpclient)
    }

}
