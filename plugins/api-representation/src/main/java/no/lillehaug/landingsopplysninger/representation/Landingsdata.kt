package no.lillehaug.landingsopplysninger.representation

import java.time.LocalDate

data class Landingsdata (val fraDato: String, val tilDato: String, val fartoy: List<String>, val leveringslinjer: List<Leveringslinje>){
    companion object {
        @JvmStatic
        fun from(linjer: List<no.lillehaug.landingsopplysninger.api.Leveringslinje>) : Landingsdata{
            val landingsdatoer = linjer.map { it.landingsdato }
            val fra = landingsdatoer.min() ?: LocalDate.of(2016, 3, 1)
            val til = landingsdatoer.max() ?: LocalDate.now()
            val fartoy = linjer.map { it.fartøy }.toSet().toList()
            val leveringslinjer = linjer.map { no.lillehaug.landingsopplysninger.representation.Leveringslinje.fromApi(it) }
            return Landingsdata(fra.toString(), til.toString(), fartoy, leveringslinjer)
        }
    }
}
