package no.lillehaug.landingsopplysninger.rest;

import no.lillehaug.landingsopplysninger.api.LandingsopplysningerRepository;
import java.time.LocalDate;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("api/landingsdata")
@Produces(MediaType.APPLICATION_JSON)
public class LandingsopplysningerResource {

    private final LandingsopplysningerRepository repository;

    LandingsopplysningerResource(LandingsopplysningerRepository repository) {
        this.repository = repository;
    }

    @Path("leveringslinjer")
    @GET
    public Response alleLeveringslinjer() {
        return Response.status(Response.Status.OK)
                /*.entity(repository
                        .alleLeveranselinjer().map { Leveringslinje.fromApi(it) })*/
                .build();
    }

    @Path("/")
    @GET
    public Response landingsdata(@QueryParam("fraDato") String fraDato,
                     @QueryParam("tilDato") String tilDato) {
        return Response.status(Response.Status.OK)
                /*.entity(Landingsdata.from(repository.alleLeveranselinjer(
                        LandingsdataQuery(localDate(fraDato), localDate(tilDato))
                )))*/
                .build();
    }

    private LocalDate localDate(String date ) {
        if(date == null) {
            return null;
        } else {
            return LocalDate.parse(date);
        }
    }
}
