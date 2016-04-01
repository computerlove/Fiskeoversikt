package no.lillehaug.landingsopplysninger;

import org.kantega.reststop.api.Export;
import org.kantega.reststop.api.Plugin;
import org.kantega.reststop.api.ServletBuilder;

import javax.servlet.Filter;

@Plugin
public class ClientPlugin {

    @Export final Filter resourceServlet;
    @Export final Filter vendorResource;
    @Export final Filter vendorMapResource;
    @Export final Filter appResource;
    @Export final Filter appMappResource;

    public ClientPlugin(ServletBuilder servletBuilder) {
        resourceServlet = servletBuilder.resourceServlet("/", getClass().getResource("/index.html"));
        vendorResource = servletBuilder.resourceServlet("/dist/vendor.bundle.js", getClass().getResource("/assets/dist/vendor.bundle.js"));
        vendorMapResource = servletBuilder.resourceServlet("/dist/vendor.bundle.js.map", getClass().getResource("/assets/dist/vendor.bundle.js.map"));
        appResource = servletBuilder.resourceServlet("/dist/app.bundle.js", getClass().getResource("/assets/dist/app.bundle.js"));
        appMappResource = servletBuilder.resourceServlet("/dist/app.bundle.js.map", getClass().getResource("/assets/dist/app.bundle.js.map"));
    }
}
