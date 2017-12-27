package no.lillehaug.landingsopplysninger.rest;

import io.swagger.jaxrs.config.BeanConfig;
import no.lillehaug.landingsopplysninger.api.LandingsopplysningerRepository;
import org.kantega.reststop.api.Export;
import org.kantega.reststop.api.Plugin;
import org.kantega.reststop.jaxrsapi.ApplicationBuilder;
import javax.ws.rs.core.Application;

@Plugin
public class LandingsopplysningerRestPlugin {

    @Export
    private final Application landingsopplysningerApp;
    private final BeanConfig beanConfig;

    public LandingsopplysningerRestPlugin(ApplicationBuilder appBuilder, LandingsopplysningerRepository repository) {
        this.landingsopplysningerApp = appBuilder.application()
                .singleton(new LandingsopplysningerResource(repository))
                .resource(io.swagger.jaxrs.listing.ApiListingResource.class)
                .resource(io.swagger.jaxrs.listing.SwaggerSerializers.class)
                .build();

        beanConfig = new BeanConfig();
        beanConfig.setVersion("1.0.2");
        beanConfig.setSchemes(new String[]{"http"});
        beanConfig.setHost("localhost:8080");
        beanConfig.setBasePath("/api");
        beanConfig.setResourcePackage("no.lillehaug.landingsopplysninger.rest");
        beanConfig.setScan(true);
    }
}
