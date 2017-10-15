package no.lillehaug.landingsopplysninger.scrape;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.codahale.metrics.health.HealthCheck;
import com.codahale.metrics.health.HealthCheckRegistry;
import no.lillehaug.landingsopplysninger.api.LandingsopplysningerRepository;
import no.lillehaug.landingsopplysninger.api.Leveringslinje;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

public class ScrapingJob {

    private static final Logger logger = LoggerFactory.getLogger(ScrapingJob.class);

    private final Timer timer = new Timer();

    private final Scraper scraper;
    private final LandingsopplysningerRepository repository;
    private final List<String> registrations;
    private final HealthCheckRegistry healthCheckRegistry;
    private final MetricRegistry metricRegistry;

    private boolean previousRunSuccess = true;

    ScrapingJob(Scraper scraper, LandingsopplysningerRepository repository, List<String> registrations, HealthCheckRegistry healthCheckRegistry, MetricRegistry metricRegistry) {
        this.scraper = scraper;
        this.repository = repository;
        this.registrations = registrations;
        this.healthCheckRegistry = healthCheckRegistry;
        this.metricRegistry = metricRegistry;

        metricRegistry.register("ScrapeTimerMetrics", timer);
        healthCheckRegistry.register("ScrapingJobHealthCheck", new HealthCheck() {
            @Override
            protected Result check() throws Exception {
                if(previousRunSuccess) {
                    return Result.healthy();
                } else {
                    return Result.unhealthy("ScrapingJob failed");
                }
            }
        });
    }

    void scrapeForRegistrations() {
        try {
            timer.time(() -> {
                try(CloseableHttpAsyncClient httpclient = HttpAsyncClients.createDefault()) {
                    logger.info("Running scrapeForRegistrations for {}", registrations);
                    for (String registration: registrations) {
                        scrapeForRegistration(registration, httpclient);
                    }

                } catch(Throwable e) {
                    previousRunSuccess = false;
                    logger.error("Error scrapeForRegistrations", e);
                }
                logger.info("Done running scrapeForRegistrations for {}", registrations);
                return null;
            });
        } catch (Exception e) {
            previousRunSuccess = false;
            logger.error("Error scrapeForRegistrations", e);
        }
    }

    private void scrapeForRegistration(String registration, CloseableHttpAsyncClient httpclient) {
        logger.info("Running scrapeForRegistrations for {}", registration);
        Mono<LocalDate> forrigeLanding = repository.forrigeLandingFor(registration);
        Flux<Leveringslinje> landinger = scraper.scrapeForRegistration(registration, httpclient);

        Flux<Leveringslinje> nyeLandinger = landinger
                .filterWhen(leveringslinje -> forrigeLanding.map(leveringslinje.landingsdato::isAfter));

        // LocalDate forrigeLanding = repository.forrigeLandingFor(registration).block();
        // Flux<Leveringslinje> nyeLandinger = landinger.filter(l -> l.landingsdato.isAfter(forrigeLanding));
        logger.info("Nye landinger: {}", nyeLandinger);
        repository.lagreLeveranselinjer(nyeLandinger);
        logger.info("Done running scrapeForRegistrations for {}", registration);
    }

    void shutdown (){
        healthCheckRegistry.unregister("ScrapingJobHealthCheck");
        metricRegistry.remove("ScrapeTimerMetrics");
    }
}
