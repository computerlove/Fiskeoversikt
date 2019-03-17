package no.lillehaug.landingsopplysninger.repository

import no.lillehaug.landingsopplysninger.repository.database.Database
import org.eclipse.microprofile.config.inject.ConfigProperty
import javax.enterprise.context.ApplicationScoped
import javax.enterprise.inject.Produces
import javax.inject.Inject

@ApplicationScoped
class RepositoryConfig(
        @Inject
        @ConfigProperty(name = "database.url") val databaseUrl: String,
        @Inject
        @ConfigProperty(name = "database.username") val databaseUser: String,
        @Inject
        @ConfigProperty(name = "database.password") val databasePassword: String) {

    @Produces
    fun database(): Database {
        return Database.createDatabase(databaseUrl, databaseUser, databasePassword)
    }
}