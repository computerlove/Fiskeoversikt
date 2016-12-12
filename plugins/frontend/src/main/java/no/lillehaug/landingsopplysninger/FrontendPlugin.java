package no.lillehaug.landingsopplysninger;

import org.kantega.reststop.api.Export;
import org.kantega.reststop.api.Plugin;
import org.kantega.reststop.servlet.api.ServletBuilder;

import javax.servlet.Filter;

@Plugin
public class FrontendPlugin {

    @Export
    private final Filter index_html;

    public FrontendPlugin(ServletBuilder servletBuilder) {
        index_html = servletBuilder.resourceServlet(getClass().getResource("/assets/index.html"), "/");
    }

}
