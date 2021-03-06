package no.lillehaug.landingsopplysninger.scrape

import com.codahale.metrics.MetricRegistry
import com.codahale.metrics.Timer
import com.codahale.metrics.health.HealthCheck
import com.codahale.metrics.health.HealthCheckRegistry
import no.lillehaug.landingsopplysninger.api.LandingsopplysningerRepository
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClients
import org.slf4j.LoggerFactory

class ScrapingJob(private val scraper: Scraper,
                  private val repository: LandingsopplysningerRepository,
                  val registrations: List<String>,
                  val healthCheckRegistry: HealthCheckRegistry,
                  val metricRegistry: MetricRegistry) {
    private val log = LoggerFactory.getLogger(javaClass)
    var previousRunSuccess = true
    private val timer = Timer()

    init {
        healthCheckRegistry.register("ScrapingJobHealthCheck", object: HealthCheck() {
            override fun check(): Result {
                return if(previousRunSuccess) {
                    Result.healthy()
                } else {
                    Result.unhealthy("ScrapingJob failed")
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
        log.debug("Landinger: {}", landinger)
        val forrigeLanding = repository.forrigeLandingFor(registration)
        log.debug("Forrige landing: {}", forrigeLanding)
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
