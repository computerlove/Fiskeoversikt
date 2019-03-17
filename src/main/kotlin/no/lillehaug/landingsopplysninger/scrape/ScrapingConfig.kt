package no.lillehaug.landingsopplysninger.scrape

import io.quarkus.scheduler.Scheduled
import no.lillehaug.landingsopplysninger.api.LandingsopplysningerRepository
import org.eclipse.microprofile.config.inject.ConfigProperty
import javax.enterprise.context.ApplicationScoped
import javax.enterprise.inject.Produces
import javax.inject.Inject

@ApplicationScoped
class ScrapingConfig {

    @Inject
    @ConfigProperty(name = "scrape.url")
    private var scrapeUrl: String = ""
    @Inject
    @ConfigProperty(name = "scrape.registrations")
    private var scrapeRegistrations: String = ""

    @Inject
    private lateinit var landingsopplysningerRepository: LandingsopplysningerRepository

    @Produces
    fun scrapeJob(): ScrapingJob {
        return ScrapingJob(Scraper(scrapeUrl), landingsopplysningerRepository, scrapeRegistrations.split(","))
    }

    //@Scheduled(every="1d")
    fun runScrapingJob(scrapingJob: ScrapingJob) {
       scrapingJob.scrapeForRegistrations()
    }

}