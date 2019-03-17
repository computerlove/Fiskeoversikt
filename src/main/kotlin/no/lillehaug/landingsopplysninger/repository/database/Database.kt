package no.lillehaug.landingsopplysninger.repository.database

import no.lillehaug.landingsopplysninger.repository.jdbc.JDBC
import org.flywaydb.core.Flyway
import org.h2.jdbcx.JdbcConnectionPool
import org.slf4j.LoggerFactory
import java.sql.Connection
import javax.sql.DataSource

class Database(val datasource : DataSource) {
    val flyway : Flyway = Flyway()

    init {
        flyway.dataSource = datasource
    }

    fun migrate() {
        flyway.setLocations("db/schema")
        flyway.migrate()
    }

    fun clean() {
        flyway.clean()
    }

    fun <T> readOnly(action: (c: Connection) -> T) : T {
        return JDBC.readOnly(datasource, action)
    }

    fun <T> transactional(action: (c: Connection) -> T) : T {
        return JDBC.transactional(datasource, action)
    }

    companion object {
        val log = LoggerFactory.getLogger(Database::class.java)

        fun createDatabase(url: String, username: String, password: String) : Database {
            return Database(createDataSource( url, username, password))
        }

        fun createTestDatabase () : Database {
            return Database(createTestDatasource())
        }

        private fun createDataSource(url: String, username: String, password: String) : DataSource {
            log.info("Creating DataSource for url: $url with username $username")
            val ds = JdbcConnectionPool.create(url, username, password)
            return ds
        }

        private fun createTestDatasource() : DataSource {
            return createDataSource("jdbc:h2:mem:test", "sa", "")
        }
    }
}
