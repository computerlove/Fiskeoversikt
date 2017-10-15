package no.lillehaug.landingsopplysninger.scrape.parse;

import com.github.tomakehurst.wiremock.WireMockServer;
import no.lillehaug.landingsopplysninger.api.Leveringslinje;
import no.lillehaug.landingsopplysninger.scrape.Scraper;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import reactor.core.publisher.Flux;
import ru.lanwen.wiremock.ext.WiremockResolver;
import ru.lanwen.wiremock.ext.WiremockUriResolver;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith({
        WiremockResolver.class,
        WiremockUriResolver.class
})
class ParserTest {


    @Test
    void parse(@WiremockResolver.Wiremock WireMockServer server, @WiremockUriResolver.WiremockUri String uri) throws IOException {
        doTest(server, uri, "/venus.html", DATA1);
    }

    @Test
    void parse2(@WiremockResolver.Wiremock WireMockServer server, @WiremockUriResolver.WiremockUri String uri) throws IOException {
        doTest(server, uri, "/english.html", DATA2);
    }

    private void doTest(@WiremockResolver.Wiremock WireMockServer server, @WiremockUriResolver.WiremockUri String uri, String path, List<Leveringslinje> expexted) throws IOException {
        server.stubFor(post(urlEqualTo(path))
                .willReturn(aResponse().withBody(getBody(path).getBytes(Parser.latin))));
        try(CloseableHttpAsyncClient httpclient = HttpAsyncClients.createDefault()) {
            httpclient.start();
            Flux<Leveringslinje> registrations = new Scraper(uri + "/" + path)
                                                     .scrapeForRegistration("N 0027SG", httpclient);

            List<Leveringslinje> parsed = registrations.toStream().collect(Collectors.toList());
            assertEquals(expexted, parsed);
        }
    }

    private static final List<Leveringslinje> DATA1 = Arrays.asList(
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 29), "Steigen Sjømat As (N869)", "Hyse", "Sluh", "-0,8 Kg", "A", 1.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 29), "Steigen Sjømat As (N869)", "Kveite", "Sluh", "-20 Kg", "A", 6.5),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 29), "Steigen Sjømat As (N869)", "Kvitlange", "Sluh", "2,0+ Kg", "A", 26.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 29), "Steigen Sjømat As (N869)", "Lyr", "Sluh", "2,0+ Kg", "A", 12.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 29), "Steigen Sjømat As (N869)", "Sei", "Sluh", "2,3+ Kg", "A", 3.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 29), "Steigen Sjømat As (N869)", "Torsk", "Sluh", "6,0+ Kg", "A", 539.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 29), "Steigen Sjømat As (N869)", "Torsk", "Sluh", "2,5+ Kg", "A", 1049.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 28), "Steigen Sjømat As (N869)", "Kvitlange", "Sluh", "2,0+ Kg", "A", 20.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 28), "Steigen Sjømat As (N869)", "Lyr", "Sluh", "2,0+ Kg", "A", 11.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 28), "Steigen Sjømat As (N869)", "Sei", "Sluh", "1,2-2,3 Kg", "A", 4.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 28), "Steigen Sjømat As (N869)", "Torsk", "Sluh", "6,0+ Kg", "A", 678.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 28), "Steigen Sjømat As (N869)", "Torsk", "Sluh", "2,5+ Kg", "Skadd", 50.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 28), "Steigen Sjømat As (N869)", "Torsk", "Sluh", "2,5+ Kg", "A", 1545.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 22), "Steigen Sjømat As (N869)", "Gråstbit", "Sluh", "1,0+ Kg", "A", 4.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 22), "Steigen Sjømat As (N869)", "Hyse", "Sluh", "-0,8 Kg", "A", 6.5),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 22), "Steigen Sjømat As (N869)", "Lyr", "Sluh", "2,0+ Kg", "A", 3.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 22), "Steigen Sjømat As (N869)", "Sei", "Sluh", "2,3+ Kg", "A", 5.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 22), "Steigen Sjømat As (N869)", "Sei", "Sluh", "1,2-2,3 Kg", "A", 5.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 22), "Steigen Sjømat As (N869)", "Torsk", "Sluh", "2,5+ Kg", "A", 260.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 22), "Steigen Sjømat As (N869)", "Torsk", "Sluh", "6,0+ Kg", "A", 225.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 22), "Steigen Sjømat As (N869)", "Torsk", "Sluh", "1,0-2,5 Kg", "A", 10.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 22), "Steigen Sjømat As (N869)", "Torsk", "Rogn", "Unspec", "Skadd", 60.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 21), "Steigen Sjømat As (N869)", "Hyse", "Sluh", "0,8+ Kg", "A", 8.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 21), "Steigen Sjømat As (N869)", "Kvitlange", "Sluh", "2,0+ Kg", "A", 2.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 21), "Steigen Sjømat As (N869)", "Sei", "Sluh", "2,3+ Kg", "A", 10.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 21), "Steigen Sjømat As (N869)", "Torsk", "Sluh", "1,0-2,5 Kg", "A", 20.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 21), "Steigen Sjømat As (N869)", "Torsk", "Sluh", "2,5+ Kg", "A", 361.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 21), "Steigen Sjømat As (N869)", "Torsk", "Rogn", "Unspec", "A", 64.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 21), "Steigen Sjømat As (N869)", "Torsk", "Sluh", "6,0+ Kg", "A", 216.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 20), "Steigen Sjømat As (N869)", "Hyse", "Sluh", "0,8+ Kg", "A", 3.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 20), "Steigen Sjømat As (N869)", "Kveite", "Sluh", "-20 Kg", "A", 6.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 20), "Steigen Sjømat As (N869)", "Kvitlange", "Sluh", "2,0+ Kg", "A", 3.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 20), "Steigen Sjømat As (N869)", "Sei", "Sluh", "1,2-2,3 Kg", "A", 15.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 20), "Steigen Sjømat As (N869)", "Sei", "Sluh", "2,3+ Kg", "A", 20.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 20), "Steigen Sjømat As (N869)", "Torsk", "Sluh", "2,5+ Kg", "A", 600.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 20), "Steigen Sjømat As (N869)", "Torsk", "Rogn", "Unspec", "A", 89.5),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 20), "Steigen Sjømat As (N869)", "Torsk", "Sluh", "6,0+ Kg", "A", 393.5),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 20), "Steigen Sjømat As (N869)", "Torsk", "Sluh", "1,0-2,5 Kg", "A", 24.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 20), "Steigen Sjømat As (N869)", "Torsk", "Sluh", "2,5+ Kg", "Skadd", 5.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 19), "Steigen Sjømat As (N869)", "Hyse", "Sluh", "-0,8 Kg", "A", 8.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 19), "Steigen Sjømat As (N869)", "Kveite", "Sluh", "-20 Kg", "A", 13.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 19), "Steigen Sjømat As (N869)", "Kvitlange", "Sluh", "2,0+ Kg", "A", 10.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 19), "Steigen Sjømat As (N869)", "Sei", "Sluh", "1,2-2,3 Kg", "A", 8.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 19), "Steigen Sjømat As (N869)", "Sei", "Sluh", "2,3+ Kg", "A", 25.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 19), "Steigen Sjømat As (N869)", "Torsk", "Rogn", "Unspec", "Skadd", 45.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 19), "Steigen Sjømat As (N869)", "Torsk", "Sluh", "6,0+ Kg", "A", 214.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 19), "Steigen Sjømat As (N869)", "Torsk", "Sluh", "1,0-2,5 Kg", "A", 24.5),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 19), "Steigen Sjømat As (N869)", "Torsk", "Sluh", "2,5+ Kg", "A", 280.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 18), "Steigen Sjømat As (N869)", "Brosme", "Sluh", "1,0-2,0 Kg", "A", 3.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 18), "Steigen Sjømat As (N869)", "Hyse", "Sluh", "-0,8 Kg", "A", 4.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 18), "Steigen Sjømat As (N869)", "Sei", "Sluh", "2,3+ Kg", "A", 5.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 18), "Steigen Sjømat As (N869)", "Sei", "Sluh", "1,2-2,3 Kg", "A", 4.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 18), "Steigen Sjømat As (N869)", "Torsk", "Sluh", "6,0+ Kg", "A", 73.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 18), "Steigen Sjømat As (N869)", "Torsk", "Sluh", "2,5+ Kg", "A", 173.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 18), "Steigen Sjømat As (N869)", "Torsk", "Rogn", "Unspec", "Skadd", 27.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 17), "Steigen Sjømat As (N869)", "Hyse", "Sluh", "0,8+ Kg", "A", 6.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 17), "Steigen Sjømat As (N869)", "Hyse", "Sluh", "-0,8 Kg", "A", 3.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 17), "Steigen Sjømat As (N869)", "Kveite", "Sluh", "-20 Kg", "A", 14.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 17), "Steigen Sjømat As (N869)", "Kvitlange", "Sluh", "0,7-2 Kg", "A", 1.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 17), "Steigen Sjømat As (N869)", "Sei", "Sluh", "2,3+ Kg", "A", 10.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 17), "Steigen Sjømat As (N869)", "Sei", "Sluh", "1,2-2,3 Kg", "A", 8.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 17), "Steigen Sjømat As (N869)", "Torsk", "Rogn", "Unspec", "A", 27.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 17), "Steigen Sjømat As (N869)", "Torsk", "Sluh", "2,5+ Kg", "A", 275.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 17), "Steigen Sjømat As (N869)", "Torsk", "Sluh", "6,0+ Kg", "A", 153.0)
    );

    private static final List<Leveringslinje> DATA2 = Arrays.asList(
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 12, 10), "Steigen Sjømat As (N869)", "Breiflabb", "Sluh", "4,0+ Kg", "A", 11.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 12, 10), "Steigen Sjømat As (N869)", "Kveite", "Sluh", "60+ Kg", "A", 99.5),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 12, 10), "Steigen Sjømat As (N869)", "Skate", "Vinger", "Unspec", "A", 2.5),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 12, 8), "Steigen Sjømat As (N869)", "Breiflabb", "Sluh", "4,0+ Kg", "A", 11.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 12, 8), "Steigen Sjømat As (N869)", "Breiflabb", "Sluh", "1,0-4,0 Kg", "A", 5.5),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 12, 8), "Steigen Sjømat As (N869)", "Kveite", "Sluh", "20-40 Kg", "A", 50.5),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 12, 3), "Steigen Sjømat As (N869)", "Breiflabb", "Sluh", "4,0+ Kg", "A", 22.0),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 12, 3), "Steigen Sjømat As (N869)", "Breiflabb", "Sluh", "1,0-4,0 Kg", "A", 3.5),
            new Leveringslinje("N 0027SG", LocalDate.of(2016, 11, 30), "Steigen Sjømat As (N869)", "Breiflabb", "Sluh", "4,0+ Kg", "A", 35.0)
    );

    private String getBody(String file) throws IOException {
        try(InputStream is = getClass().getResourceAsStream("/bodies/" + file)){
            return Parser.read(is);
        }
    }
}
