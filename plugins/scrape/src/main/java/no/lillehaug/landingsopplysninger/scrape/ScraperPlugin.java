package no.lillehaug.landingsopplysninger.scrape;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.health.HealthCheckRegistry;
import no.lillehaug.landingsopplysninger.api.LandingsopplysningerRepository;
import org.kantega.reststop.api.Config;
import org.kantega.reststop.api.Plugin;

import javax.annotation.PreDestroy;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.util.Arrays.asList;

@Plugin
public class ScraperPlugin {

    private final ScheduledExecutorService executorService;
    private final ScrapingJob scraperJob;

    public ScraperPlugin(@Config(doc = "Url to POST registrations to", property = "scrape.url") String scrapeUrl,
                         @Config(doc = "Vessel registrations, comma separated", property = "scrape.registrations") String scrapeRegistrations,
                         @Config(doc = "Initial delay for running scraping job", property = "scrape.job.delay", defaultValue = "1") Integer scrapeJobDelay,
                         @Config(doc = "Period with which scraping job should run", property = "scrape.job.period", defaultValue = "1") Integer scrapeJobPeriod,
                         @Config(doc = "Time unit (java.util.concurrent.TimeUnit) for scrapting job period", property = "scrape.job.timeunit", defaultValue = "DAYS") String scrapeJobTimeUnit,
                         LandingsopplysningerRepository landingsopplysningerRepository,
                         HealthCheckRegistry healthCheckRegistry,
                         MetricRegistry metricRegistry) {

        scraperJob = new ScrapingJob(new Scraper(scrapeUrl), landingsopplysningerRepository, asList(scrapeRegistrations.split(",")), healthCheckRegistry, metricRegistry);
        executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(scraperJob::scrapeForRegistrations, scrapeJobDelay, scrapeJobPeriod, TimeUnit.valueOf(scrapeJobTimeUnit));
    }

    @PreDestroy
    public void shutdown() {
        scraperJob.shutdown();
        executorService.shutdown();
    }
}
