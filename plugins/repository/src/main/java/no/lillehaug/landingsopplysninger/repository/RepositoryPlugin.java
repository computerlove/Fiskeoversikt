package no.lillehaug.landingsopplysninger.repository;

import no.lillehaug.landingsopplysninger.api.LandingsopplysningerRepository;
import org.kantega.reststop.api.Export;
import org.kantega.reststop.api.Plugin;

@Plugin
public class RepositoryPlugin {
    @Export
    final LandingsopplysningerRepository repository;

    public RepositoryPlugin() {
        repository = new InMemoryRepository();
    }
}
