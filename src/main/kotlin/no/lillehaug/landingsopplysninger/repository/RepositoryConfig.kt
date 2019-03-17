package no.lillehaug.landingsopplysninger.repository

import no.lillehaug.landingsopplysninger.api.LandingsopplysningerRepository
import no.lillehaug.landingsopplysninger.repository.database.Database
import org.eclipse.microprofile.config.inject.ConfigProperty
import javax.enterprise.context.ApplicationScoped
import javax.enterprise.inject.Produces
import javax.inject.Inject

@ApplicationScoped
class RepositoryConfig {

    @Inject
    @ConfigProperty(name = "database.url")
    private var databaseUrl: String = ""

    @Inject
    @ConfigProperty(name = "database.username")
    private var databaseUser: String = ""

    @Inject
    @ConfigProperty(name = "database.password", defaultValue = "")
    private var databasePassword: String = ""

    @Produces
    fun database(): Database {
        val database = Database.createDatabase(databaseUrl, databaseUser, databasePassword)
        database.migrate()
        return database
    }

    @Produces
    fun repository(database: Database) : LandingsopplysningerRepository {
        return JdbcRepository(database)
    }
}