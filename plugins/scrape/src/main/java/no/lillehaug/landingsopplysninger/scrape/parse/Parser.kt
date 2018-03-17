package no.lillehaug.landingsopplysninger.scrape.parse

import no.lillehaug.landingsopplysninger.api.Leveringslinje
import org.apache.commons.io.IOUtils
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.message.BasicNameValuePair
import org.jsoup.Jsoup
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.util.*
import javax.script.ScriptEngineManager

val jsPattern = ".*innerHTML = \"(.*)\";.*".toRegex(RegexOption.MULTILINE).toPattern()
val tagPattern = "\\</?[^>]*\\>".toRegex().toPattern()
val engine = ScriptEngineManager().getEngineByName("nashorn")

class Parser (private val url: String){

    fun fetchAndParseForRegistration(registration: String, httpclient: CloseableHttpClient) : List<Leveringslinje> {

        val get = httpclient.execute(HttpGet(url)).use {
            val html = IOUtils.toString(it.entity.content, "ISO-8859-1")
            parseForm(html)
        }

        if(get == null) emptyList<Leveringslinje>()

        val entity = UrlEncodedFormEntity(
                listOf(
                        BasicNameValuePair("regmerke", registration),
                        BasicNameValuePair("svar", get?.second)
                ))

        val action = get?.first
        val httpPost = HttpPost("http://www.rafisklaget.no${action}")
        httpPost.entity = entity

        return httpclient.execute(httpPost).use {
            val html = IOUtils.toString(it.entity.content, "ISO-8859-1")
            parseFromHtml(html, registration)
        }
    }

    private fun parseForm(html: String): Pair<String, String>? {
        val parsed = Jsoup.parse(html)
        val form = parsed.getElementById("reportfilter")
        val action = form.attr("action")

        val elementsByTag = parsed.getElementsByTag("script")
        val script = elementsByTag[0]
        val text = script.data().split("\n")
                .firstOrNull { it.contains("innerHTML = ") }
        if(!text.isNullOrBlank()) {
            val matcher = jsPattern.matcher(text)
            if(matcher.matches()) {
                val expression = matcher.group(1)
                val stripped = tagPattern.matcher(expression)
                        .replaceAll("")
                        .replace("=", "")
                val eval = engine.eval(stripped)

                return Pair(action, eval.toString())
            }
        }
        return null
    }

    fun parseFromHtml(html: String, fartøy: String)  : List<Leveringslinje> {
        val document = Jsoup.parse(html)
        val tables = document.getElementsByTag("table")
        val lines = tables.select("tr")

        val entries = mutableListOf<Entry>()
        var previousLandingsdato = "Ukjent"
        var previousMottak = "Ukjent"

        for (line in lines) {
            val columns = line.select("td")
            if (columns.size > 0) {
                val landingsdato = columns[0].html()
                val mottak = columns[1].html()
                val fiskeslag = columns[2].html()
                val tilstand = columns[3].html()
                val størrelse = columns[4].html()
                val kvalitet = columns[5].html()
                val nettovekt = columns[6].html()

                previousLandingsdato = defaultOrValue(previousLandingsdato, landingsdato)
                previousMottak = defaultOrValue(previousMottak, mottak)
                entries.add(Entry(fartøy, previousLandingsdato, previousMottak, fiskeslag, tilstand, størrelse, kvalitet, nettovekt))
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

    private val dateParsers = listOf(
            DateTimeFormatter.ofPattern("dd.MM.yyyy"),
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
                // lol
            }
        }
        throw IllegalStateException("No parsers for ${landingsDato}")
    }

    private fun defaultOrValue(defaultValue: String, value: String? ) : String {
        return if (value == null || value.isBlank() || value == "&nbsp;") {
            defaultValue
        } else {
            value
        }
    }

    data class Entry(val fartøy: String, val landingsdato: String, val mottak: String, val fiskeslag: String, val tilstand: String, val størrelse: String, val kvalitet: String, val nettovekt: String)
}
