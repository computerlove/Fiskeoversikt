package no.lillehaug.landingsopplysninger.representation

import no.lillehaug.landingsopplysninger.api.LeveringslinjeWithId

data class LandingsdataByDate(val landingsdata: Map<String, Map<String, List<Leveringslinje>>>) {
    companion object {
        @JvmStatic
        fun from(l: List<LeveringslinjeWithId>): LandingsdataByDate {
            val groupByFartøy = l
                    .groupBy { it.fartøy }
                    .mapValues {
                        it.value.map { Leveringslinje.fromApi(it) }
                                .groupBy { it.landingsdato } }

            return LandingsdataByDate(groupByFartøy)
        }
    }
}