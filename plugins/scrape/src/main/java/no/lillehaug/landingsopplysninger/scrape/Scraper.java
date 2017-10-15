package no.lillehaug.landingsopplysninger.scrape;

import no.lillehaug.landingsopplysninger.api.Leveringslinje;
import no.lillehaug.landingsopplysninger.scrape.parse.Parser;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import reactor.core.publisher.Flux;

public class Scraper {
    private final String url;

    public Scraper(String url) {
        this.url = url;
    }

    public Flux<Leveringslinje> scrapeForRegistration(String registration, CloseableHttpAsyncClient client) {
        return new Parser(url).fetchAndParseForRegistration(registration, client);
    }
}
