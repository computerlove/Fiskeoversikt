package no.lillehaug.landingsopplysninger.scrape;

import org.kantega.reststop.api.Config;
import org.kantega.reststop.api.Export;
import org.kantega.reststop.api.Plugin;

@Plugin
public class ScraperPlugin {

    @Export
    private final Scraper scraper;

    public ScraperPlugin(@Config String scrapeUrl) {
        this.scraper = new Scraper(scrapeUrl);
    }
}
