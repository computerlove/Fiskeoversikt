package no.lillehaug.landingsopplysninger.rest;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import no.lillehaug.landingsopplysninger.api.LandingsopplysningerRepository;
import no.lillehaug.landingsopplysninger.api.LeveringslinjeWithId;

import java.io.IOException;
import java.time.LocalDate;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

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

        StreamingOutput so = (output) -> {
            JsonGenerator generator = new JsonFactory().createGenerator(output);
            generator.writeStartArray();

            repository.alleLeveranselinjer()
                    .toStream()
                    .forEach((LeveringslinjeWithId linje) -> {
                        try {
                            generator.writeStartObject();

                            generator.writeStringField("uuid", linje.id.toString());
                            generator.writeStringField("fartøy", linje.fartøy);
                            generator.writeStringField("landingsdato", linje.landingsdato.toString());
                            generator.writeStringField("mottak", linje.mottak);
                            generator.writeStringField("fiskeslag", linje.fiskeslag);
                            generator.writeStringField("tilstand", linje.tilstand);
                            generator.writeStringField("størrelse", linje.størrelse);
                            generator.writeStringField("kvalitet", linje.kvalitet);
                            generator.writeNumberField("nettovekt", linje.nettovekt);

                            generator.writeEndObject();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });

            generator.writeEndArray();
        };
        return Response.status(Response.Status.OK)
                .entity(so)
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
