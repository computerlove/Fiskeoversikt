package no.lillehaug.landingsopplysninger.scrape.parse;

import no.lillehaug.landingsopplysninger.api.Leveringslinje;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Parser {
    private static final Logger logger = LoggerFactory.getLogger(Parser.class);
    private static final Pattern vesselPattern = Pattern.compile("[^(]*\\((.*)\\)");
    public static final Charset latin = Charset.forName("ISO-8859-1");

    private final String url;

    public Parser(String url) {
        this.url = url;
    }

    public Flux<Leveringslinje> fetchAndParseForRegistration(String registration, CloseableHttpAsyncClient client) {
        return Flux.create(sink -> {
            doPost(registration, client, sink);
        });
    }

    private void doPost(String registration, CloseableHttpAsyncClient client, FluxSink<Leveringslinje> sink) {
        HttpPost post = new HttpPost(url);
        post.setEntity(new UrlEncodedFormEntity(Arrays.asList(
                new BasicNameValuePair("p_arg_names", "p_regmerke"),
                new BasicNameValuePair("p_arg_values", registration)
        ), latin));
        FutureCallback<HttpResponse> callback = new FutureCallback<HttpResponse>() {
            @Override
            public void completed(HttpResponse result) {
                try {
                    String html = read(result.getEntity().getContent());
                    parseHtml(html, sink);
                    sink.complete();
                } catch (IOException e) {
                    logger.error("Error reading response", e);
                    sink.error(e);
                }
            }

            @Override
            public void failed(Exception ex) {
                logger.error("Request failed", ex);
                sink.error(ex);
            }

            @Override
            public void cancelled() {
                sink.complete();
            }
        };
        client.execute(post, callback);
    }

    private void parseHtml(String html, FluxSink<Leveringslinje> sink) {
        Document document = Jsoup.parse(html);
        Elements tables = document.getElementsByTag("table");
        Elements lines = tables.select("tr");

        String previousFartøy = "Ukjent";
        String previousLandingsdato = "Ukjent";
        String previousMottak= "Ukjent";

        for (Element line : lines) {
            Elements columns = line.select("td");
            if (columns.size() > 0) {

                String fartøy = columns.get(0).html();
                String landingsdato = columns.get(1).html();
                String mottak = columns.get(2).html();
                String fiskeslag = columns.get(3).html();
                String tilstand = columns.get(4).html();
                String størrelse = columns.get(5).html();
                String kvalitet = columns.get(6).html();
                String nettovekt = columns.get(7).html();
                Matcher matcher = vesselPattern.matcher(fartøy);
                String fartøyKjennemerke = matcher.matches() ? matcher.group(1) : fartøy;

                previousFartøy = defaultOrValue(previousFartøy, fartøyKjennemerke);
                previousLandingsdato = defaultOrValue(previousLandingsdato, landingsdato);
                previousMottak = defaultOrValue(previousMottak, mottak);

                sink.next(new Leveringslinje(
                                             previousFartøy,
                                             tryParseDate(previousLandingsdato),
                                             previousMottak,
                                             fiskeslag,
                                             tilstand,
                                             størrelse,
                                             kvalitet,
                                             Double.valueOf(nettovekt)));
            }
        }


    }

    private static final List<DateTimeFormatter> dataParsers = Arrays.asList(
            new DateTimeFormatterBuilder()
                    .parseCaseInsensitive()
                    .appendPattern("dd-MMM-yy")
                    .toFormatter(),
            new DateTimeFormatterBuilder()
                    .parseCaseInsensitive()
                    .appendPattern("dd-MMM-yy")
                    .toFormatter(Locale.forLanguageTag("NO")));
    private LocalDate tryParseDate(String landingsDato) {

        for (DateTimeFormatter parser: dataParsers) {
            try {
                return LocalDate.parse(landingsDato, parser);
            } catch(Exception e) {
                // try next
            }
        }
        throw new IllegalStateException("No parsers for ${landingsDato}");
    }

    private String defaultOrValue(String defaultValue, String value){
        if (value == null || value.trim().isEmpty() || value.equals("&nbsp;")) {
            return defaultValue;
        } else {
            return value;
        }
    }

    public static String read(InputStream input) throws IOException {
        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(input, latin))) {
            return buffer.lines().collect(Collectors.joining("\n"));
        }
    }
}
