package no.lillehaug.landingsopplysninger.rest

import no.lillehaug.landingsopplysninger.api.LandingsopplysningerRepository
import no.lillehaug.landingsopplysninger.representation.Leveringslinje
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.core.Response

@Path("landingsopplysninger")
class LandingsopplysningerResource (val repository: LandingsopplysningerRepository){

    @GET
    fun alleLeveringslinjer() : Response {
        return Response.status(Response.Status.OK).entity(
                repository
                        .alleLeveranselinjer().map { Leveringslinje.fromApi(it) })
                .build()
    }
}
