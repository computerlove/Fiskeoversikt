package no.lillehaug.landingsopplysninger.api

data class LandingsdataQuery(val fraDato: String?, val tilDato: String?, val fartøy: List<String>)