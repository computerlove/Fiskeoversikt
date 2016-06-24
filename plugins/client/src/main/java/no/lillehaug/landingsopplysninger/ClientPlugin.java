package no.lillehaug.landingsopplysninger;

import org.kantega.reststop.api.Export;
import org.kantega.reststop.api.Plugin;
import org.kantega.reststop.api.ServletBuilder;

import javax.servlet.Filter;

@Plugin
public class ClientPlugin {

    @Export final Filter resourceServlet;

    public ClientPlugin(ServletBuilder servletBuilder) {
        resourceServlet = servletBuilder.resourceServlet("/", getClass().getResource("/assets/index.html"));
    }
}
