package no.lillehaug.landingsopplysninger.scrape.parse

import no.lillehaug.landingsopplysninger.api.Leveringslinje
import org.apache.commons.io.IOUtils
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.HttpClients
import org.apache.http.message.BasicNameValuePair
import org.jsoup.Jsoup
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class Parser (url: String){
    val url = url
    val httpclient = HttpClients.createDefault();

    fun fetchAndParseForRegistration(registration: String) : List<Leveringslinje> {

        val entity = UrlEncodedFormEntity(
                listOf(
                        BasicNameValuePair("p_arg_names", "p_regmerke"),
                        BasicNameValuePair("paramValues", registration)
                ))

        val httpPost = HttpPost(url)
        httpPost.entity = entity
        val response = httpclient.execute(httpPost)
        var html = IOUtils.toString(response.entity.content)
        return parseFromHtml(html)
    }

    fun parseFromHtml(html: String)  : List<Leveringslinje> {
        val document = Jsoup.parse(html)
        val tables = document.getElementsByTag("table")
        val lines = tables.select("tr")
        val header = lines.removeAt(0)

        val entries = mutableListOf<Entry>()
        var previousFartøy : String = "Ukjent"
        var previousLandingsdato : String = "Ukjent"
        var previousMottak : String = "Ukjent"
        for (line in lines) {
            val columns = line.select("td")
            val fartøy = columns[0].html()
            val landingsdato = columns[1].html()
            val mottak = columns[2].html()
            val fiskeslag = columns[3].html()
            val tilstand = columns[4].html()
            val størrelse = columns[5].html()
            val kvalitet = columns[6].html()
            val nettovekt = columns[7].html()

            previousFartøy = defaultOrValue(previousFartøy, fartøy)
            previousLandingsdato = defaultOrValue(previousLandingsdato, landingsdato)
            previousMottak = defaultOrValue(previousMottak, mottak)

            entries.add(Entry(previousFartøy, previousLandingsdato, previousMottak, fiskeslag, tilstand, størrelse, kvalitet, nettovekt))
        }

        val datePattern = DateTimeFormatter.ofPattern("dd-MMM-yy", Locale.forLanguageTag("NO"))
        return entries.map {
            Leveringslinje(
                    it.fartøy,
                    LocalDate.parse(it.landingsdato.toLowerCase(), datePattern),
                    it.mottak,
                    it.fiskeslag,
                    it.tilstand,
                    it.størrelse,
                    it.kvalitet,
                    it.nettovekt.toDouble()
            )
        }
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
