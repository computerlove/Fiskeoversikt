package no.lillehaug.landingsopplysninger.representation

import no.lillehaug.landingsopplysninger.api.LeveringslinjeWithId

data class LandingsdataByDate(val fartøy: String, val landingsdato: String, val leveringslinjer: List<Leveringslinje>) {
    companion object {
        @JvmStatic
        fun from(l: List<LeveringslinjeWithId>): List<LandingsdataByDate> {
            val groupByFartøy = l
                    .groupBy { it.fartøy }
                    .mapValues {
                        it.value.groupBy { it.landingsdato } }

            return groupByFartøy.flatMap {
                val fartøy = it.key
                it.value.map {
                    LandingsdataByDate(fartøy, it.key.toString(), it.value.map { Leveringslinje.fromApi(it) })
                }
            }
        }
    }
}