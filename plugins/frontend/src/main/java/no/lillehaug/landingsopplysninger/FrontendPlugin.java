package no.lillehaug.landingsopplysninger;

import org.kantega.reststop.api.Export;
import org.kantega.reststop.api.Plugin;
import org.kantega.reststop.servlet.api.ServletBuilder;

import javax.servlet.Filter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;

@Plugin
public class FrontendPlugin {

    @Export
    private final Filter index_html;

    public FrontendPlugin(ServletBuilder servletBuilder) {
        URL resource = getClass().getResource("/assets/index.html");
        index_html = servletBuilder.servlet(
                new HttpServlet() {
                    @Override
                    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

                    }
                }, "/");
    }

}
