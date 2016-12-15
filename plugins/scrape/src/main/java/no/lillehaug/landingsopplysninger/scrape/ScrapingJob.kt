package no.lillehaug.landingsopplysninger.scrape

import no.lillehaug.landingsopplysninger.api.LandingsopplysningerRepository
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClients
import org.slf4j.LoggerFactory

class ScrapingJob(val scraper: Scraper, val repository: LandingsopplysningerRepository, val registrations : List<String>) {
    val log = LoggerFactory.getLogger(javaClass)

    fun scrapeForRegistrations() {
        try {
            val httpclient = HttpClients.createDefault();

            log.info("Running scrapeForRegistrations for {}", registrations)
            for (registration in registrations) {
                scrapeForRegistration(registration, httpclient)
            }
            // TODO use trywr
            httpclient.close()
        } catch(e: Throwable) {
            log.error("Error scrapeForRegistrations", e)
        }
        log.info("Done running scrapeForRegistrations for {}", registrations)

    }

    private fun scrapeForRegistration(registration: String, httpclient: CloseableHttpClient) {
        log.info("Running scrapeForRegistrations for {}", registration)
        val landinger = scraper.scrapeForRegistration(registration, httpclient)
        val forrigeLanding = repository.forrigeLandingFor(registration)
        val nyeLandinger = landinger.filter { it.landingsdato.isAfter(forrigeLanding) }
        log.info("Nye landinger: {}", nyeLandinger)
        repository.lagreLeveranselinjer(nyeLandinger)
        log.info("Done running scrapeForRegistrations for {}", registration)
    }
}
