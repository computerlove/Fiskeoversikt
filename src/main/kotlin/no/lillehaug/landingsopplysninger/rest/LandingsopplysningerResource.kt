package no.lillehaug.landingsopplysninger.rest

import no.lillehaug.landingsopplysninger.api.LandingsopplysningerRepository
import no.lillehaug.landingsopplysninger.representation.LandingsdataByDateAndVessel
import no.lillehaug.landingsopplysninger.representation.Leveringslinje
import java.time.LocalDate
import javax.inject.Inject
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("api/landingsdata")
@Produces(MediaType.APPLICATION_JSON)
class LandingsopplysningerResource (@Inject private val repository: LandingsopplysningerRepository){

    @Path("leveringslinjer")
    @GET
    fun alleLeveringslinjer() : Response {
        return Response.status(Response.Status.OK)
                .entity(repository
                        .alleLeveranselinjer().map { Leveringslinje.fromApi(it) })
                .build()
    }

    @Path("/")
    @GET
    fun landingsdata(@QueryParam("num") @DefaultValue("3") num: Int,
                     @QueryParam("start") @DefaultValue("0") start: Int) : Response {
        return Response.status(Response.Status.OK)
                .entity(LandingsdataByDateAndVessel.from(repository
                            .alleLeveranselinjerByDates(num, start)
                ))
                .build()
    }

    private fun localDate(date: String? ) : LocalDate? {
        return if(date == null) {
            null
        } else {
            LocalDate.parse(date)
        }
    }
}
