package no.lillehaug.landingsopplysninger.scrape

import no.lillehaug.landingsopplysninger.api.LandingsopplysningerRepository
import org.eclipse.microprofile.config.inject.ConfigProperty
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import javax.annotation.PreDestroy
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
class ScrapingConfig(
        @Inject
        @ConfigProperty(name = "scrape.url") val scrapeUrl: String,
        @Inject
        @ConfigProperty(name = "scrape.registrations") val scrapeRegistrations: String,
        @Inject
        @ConfigProperty(name = "scrape.job.delay", defaultValue = "1") val scrapeJobDelay: Int,
        @Inject
        @ConfigProperty(name = "scrape.job.period", defaultValue = "1") val scrapeJobPeriod: Int,
        @Inject
        @ConfigProperty(name = "scrape.job.timeunit", defaultValue = "DAYS") val scrapeJobTimeUnit: String,
        @Inject val landingsopplysningerRepository: LandingsopplysningerRepository) {

    val executorService = Executors.newSingleThreadScheduledExecutor()
    val scrapingJob = ScrapingJob(Scraper(scrapeUrl), landingsopplysningerRepository, scrapeRegistrations.split(","))

    init {
        executorService.scheduleAtFixedRate({ scrapingJob.scrapeForRegistrations() }, scrapeJobDelay.toLong(), scrapeJobPeriod.toLong(), TimeUnit.valueOf(scrapeJobTimeUnit))
    }

    @PreDestroy
    fun shutdown() {
        executorService.shutdown()
    }
}