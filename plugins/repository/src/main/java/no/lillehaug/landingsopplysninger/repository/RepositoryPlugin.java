package no.lillehaug.landingsopplysninger.repository;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.health.HealthCheckRegistry;
import no.lillehaug.landingsopplysninger.api.LandingsopplysningerRepository;
import no.lillehaug.landingsopplysninger.repository.database.Database;
import org.kantega.reststop.api.Config;
import org.kantega.reststop.api.Export;
import org.kantega.reststop.api.Plugin;

import javax.annotation.PreDestroy;

@Plugin
public class RepositoryPlugin {

    @Export
    final LandingsopplysningerRepository repository;

    private final Database database;

    public RepositoryPlugin(
            @Config(doc = "Class of database driver", property = "database.driver") String databaseDriver,
            @Config(doc = "JDBC connection url", property = "database.url") String databaseUrl,
            @Config(doc = "JDBC database username", property = "database.username") String databaseUser,
            @Config(doc = "JDBC database password", property = "database.password", required = false) String databasePassword,
            HealthCheckRegistry healthCheckRegistry,
            MetricRegistry metricRegistry) {

        database = Database.Companion.createDatabase(databaseDriver, databaseUrl, databaseUser, databasePassword, healthCheckRegistry, metricRegistry);
        database.migrate();
        repository = new JdbcRepository(database);
    }

    @PreDestroy
    public void shutDown(){
        database.close();
    }
}
