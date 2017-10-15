package no.lillehaug.landingsopplysninger.rest

import no.lillehaug.landingsopplysninger.representation.Landingsdata
import no.lillehaug.landingsopplysninger.representation.Leveringslinje
import java.time.LocalDate
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.QueryParam
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("api/landingsdata")
@Produces(MediaType.APPLICATION_JSON)
class LandingsopplysningerResource (val repository: LandingsopplysningerRepository){

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
    fun landingsdata(@QueryParam("fraDato") fraDato: String?,
                     @QueryParam("tilDato") tilDato: String?) : Response {
        return Response.status(Response.Status.OK)
                .entity(Landingsdata.from(repository.alleLeveranselinjer(
                        LandingsdataQuery(localDate(fraDato), localDate(tilDato))
                )))
                .build()
    }

    private fun localDate(date: String? ) : LocalDate? {
        if(date == null) {
            return null
        } else {
            return LocalDate.parse(date)
        }
    }
}
