package no.lillehaug.landingsopplysninger.scrape.parse

import no.lillehaug.landingsopplysninger.api.Leveringslinje
import org.apache.commons.io.IOUtils
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.message.BasicNameValuePair
import org.jsoup.Jsoup
import java.time.LocalDate
import java.time.format.DateTimeFormatterBuilder
import java.util.*

val vesselPattern = "[^(]*\\((.*)\\)".toRegex().toPattern()

class Parser (val url: String){

    fun fetchAndParseForRegistration(registration: String, httpclient: CloseableHttpClient) : List<Leveringslinje> {

        val entity = UrlEncodedFormEntity(
                listOf(
                        BasicNameValuePair("p_arg_names", "p_regmerke"),
                        BasicNameValuePair("p_arg_values", registration)
                ))

        val httpPost = HttpPost(url)
        httpPost.entity = entity

        val response = httpclient.execute(httpPost)
        val html = IOUtils.toString(response.entity.content, "ISO-8859-1")
        return parseFromHtml(html)
    }

    fun parseFromHtml(html: String)  : List<Leveringslinje> {
        val document = Jsoup.parse(html)
        val tables = document.getElementsByTag("table")
        val lines = tables.select("tr")

        val entries = mutableListOf<Entry>()
        var previousFartøy : String = "Ukjent"
        var previousLandingsdato : String = "Ukjent"
        var previousMottak : String = "Ukjent"
        for (line in lines) {
            val columns = line.select("td")
            if (columns.size > 0) {
                val fartøy = columns[0].html()
                val landingsdato = columns[1].html()
                val mottak = columns[2].html()
                val fiskeslag = columns[3].html()
                val tilstand = columns[4].html()
                val størrelse = columns[5].html()
                val kvalitet = columns[6].html()
                val nettovekt = columns[7].html()

                val matcher = vesselPattern.matcher(fartøy)
                val fartøyKjennemerke = if(matcher.matches()) matcher.group(1) else fartøy

                previousFartøy = defaultOrValue(previousFartøy, fartøyKjennemerke)
                previousLandingsdato = defaultOrValue(previousLandingsdato, landingsdato)
                previousMottak = defaultOrValue(previousMottak, mottak)
                entries.add(Entry(previousFartøy, previousLandingsdato, previousMottak, fiskeslag, tilstand, størrelse, kvalitet, nettovekt))
            }
        }

        return entries.map {
            Leveringslinje(
                    it.fartøy,
                    tryParseDate(it),
                    it.mottak,
                    it.fiskeslag,
                    it.tilstand,
                    it.størrelse,
                    it.kvalitet,
                    it.nettovekt.toDouble()
            )
        }
    }

    val dateParsers = listOf(
            DateTimeFormatterBuilder()
                    .parseCaseInsensitive()
                    .appendPattern("dd-MMM-yy")
                    .toFormatter(),
            DateTimeFormatterBuilder()
                    .parseCaseInsensitive()
                    .appendPattern("dd-MMM-yy")
                    .toFormatter(Locale.forLanguageTag("NO")))
    private fun tryParseDate(it: Entry) : LocalDate {

        val landingsDato = it.landingsdato
        for (parser in dateParsers) {
            try {
                return LocalDate.parse(landingsDato, parser)
            } catch(e: Exception) {
                throw e
            }
        }
        throw IllegalStateException("No parsers for ${landingsDato}")
    }

    private fun defaultOrValue(defaultValue: String, value: String? ) : String {
        if (value == null || value.isBlank() || value.equals("&nbsp;")) {
            return defaultValue
        } else {
            return value
        }
    }

    data class Entry(val fartøy: String, val landingsdato: String, val mottak: String, val fiskeslag: String, val tilstand: String, val størrelse: String, val kvalitet: String, val nettovekt: String)
}
