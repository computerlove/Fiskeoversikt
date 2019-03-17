package no.lillehaug.landingsopplysninger.representation

import no.lillehaug.landingsopplysninger.api.LeveringslinjeWithId

data class LandingsdataByDateAndVessel(val fartøy: String,
                                       val landingsdato: String,
                                       val leveringslinjer: List<Leveringslinje>) {
    companion object {
        @JvmStatic
        fun from(linjer: List<LeveringslinjeWithId>): List<LandingsdataByDateAndVessel> {
            return linjer.groupBy { it.landingsdato }
                    .mapValues {
                        val dato = it.key
                        it.value.groupBy { it.fartøy }
                                .mapValues {
                                    val f = it.key
                                    LandingsdataByDateAndVessel(f, dato.toString(), it.value.map { Leveringslinje.fromApi(it) })
                                }
                    }.flatMap { it.value.values }
        }
    }
}