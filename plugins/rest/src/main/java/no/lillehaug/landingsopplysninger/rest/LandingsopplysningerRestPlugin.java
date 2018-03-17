package no.lillehaug.landingsopplysninger.rest;

import no.lillehaug.landingsopplysninger.api.LandingsopplysningerRepository;
import org.kantega.reststop.api.Export;
import org.kantega.reststop.api.Plugin;
import org.kantega.reststop.jaxrsapi.ApplicationBuilder;
import javax.ws.rs.core.Application;

@Plugin
public class LandingsopplysningerRestPlugin {

    @Export
    private final Application landingsopplysningerApp;

    public LandingsopplysningerRestPlugin(ApplicationBuilder appBuilder, LandingsopplysningerRepository repository) {
        this.landingsopplysningerApp = appBuilder.application()
                .singleton(new LandingsopplysningerResource(repository))
                .build();

    }
}
