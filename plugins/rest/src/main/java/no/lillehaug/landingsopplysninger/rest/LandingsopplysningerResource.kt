package no.lillehaug.landingsopplysninger.rest

import no.lillehaug.landingsopplysninger.api.LandingsopplysningerRepository
import no.lillehaug.landingsopplysninger.representation.Landingsdata
import no.lillehaug.landingsopplysninger.representation.Leveringslinje
import java.time.LocalDate
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.QueryParam
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("landingsdata")
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
                     @QueryParam("tilDato") tilDato: String?,
                     @QueryParam("fartoy") fartoy: List<String> ) : Response {
        val landingsdata = Landingsdata.from(repository
                .alleLeveranselinjer())
        return Response.status(Response.Status.OK)
                .entity(landingsdata)
                .build()
    }
}
