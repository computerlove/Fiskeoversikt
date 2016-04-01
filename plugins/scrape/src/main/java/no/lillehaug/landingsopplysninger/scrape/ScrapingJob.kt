package no.lillehaug.landingsopplysninger.scrape

import no.lillehaug.landingsopplysninger.api.LandingsopplysningerRepository
import org.slf4j.LoggerFactory

class ScrapingJob(val scraper: Scraper, val repository: LandingsopplysningerRepository, val registrations : List<String>) {
    val log = LoggerFactory.getLogger(javaClass)

    fun scrapeForRegistrations() {
        log.info("Running scrapeForRegistrations")
        registrations.forEach {
            scrapeForRegistration(it)
        }
    }

    private fun scrapeForRegistration(registration: String) {
        val landinger = scraper.scrapeForRegistration(registration)
        val forrigeLanding = repository.forrigeLandingFor(registration)
        val nyeLandinger = landinger.filter { it.landingsdato.isAfter(forrigeLanding) }
        log.info("Nye landinger: {}", nyeLandinger)
        repository.lagreLeveranselinjer(nyeLandinger)
    }
}
