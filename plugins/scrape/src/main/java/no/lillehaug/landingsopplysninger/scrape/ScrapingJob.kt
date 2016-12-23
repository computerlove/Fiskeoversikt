package no.lillehaug.landingsopplysninger.scrape

import com.codahale.metrics.MetricRegistry
import com.codahale.metrics.Timer
import com.codahale.metrics.health.HealthCheck
import com.codahale.metrics.health.HealthCheckRegistry
import no.lillehaug.landingsopplysninger.api.LandingsopplysningerRepository
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClients
import org.slf4j.LoggerFactory

class ScrapingJob(val scraper: Scraper, val repository: LandingsopplysningerRepository, val registrations: List<String>, val healthCheckRegistry: HealthCheckRegistry, val metricRegistry: MetricRegistry) {
    val log = LoggerFactory.getLogger(javaClass)
    var previousRunSuccess = true;
    val timer = Timer()

    init {
        healthCheckRegistry.register("ScrapingJobHealthCheck", object: HealthCheck() {
            override fun check(): Result {
                if(previousRunSuccess) {
                    return Result.healthy()
                } else {
                    return Result.unhealthy("ScrapingJob failed")
                }
            }
        })
        metricRegistry.register("ScrapeTimerMetrics", timer)
    }

    fun scrapeForRegistrations() {
        timer.time({
            try {
                val httpclient = HttpClients.createDefault();

                log.info("Running scrapeForRegistrations for {}", registrations)
                for (registration in registrations) {
                    scrapeForRegistration(registration, httpclient)
                }
                // TODO use trywr
                httpclient.close()
            } catch(e: Throwable) {
                previousRunSuccess = false
                log.error("Error scrapeForRegistrations", e)
            }
            log.info("Done running scrapeForRegistrations for {}", registrations)
        })
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

    fun shutdown (){
        healthCheckRegistry.unregister("ScrapingJobHealthCheck")
        metricRegistry.remove("ScrapeTimerMetrics")
    }
}
