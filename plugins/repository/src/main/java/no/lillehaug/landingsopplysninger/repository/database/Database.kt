package no.lillehaug.landingsopplysninger.repository.database

import com.zaxxer.hikari.HikariDataSource
import no.lillehaug.landingsopplysninger.repository.jdbc.JDBC
import org.flywaydb.core.Flyway
import org.slf4j.LoggerFactory
import java.sql.Connection
import javax.sql.DataSource

class Database(val datasource : DataSource) {
    val flyway : Flyway

    init {
        flyway = Flyway()
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

        fun createDatabase(driver: String, url: String, username: String, password: String) : Database {
            return Database(createDataSource(driver, url, username, password))
        }

        fun createTestDatabase () : Database {
            return Database(createTestDatasource())
        }

        private fun createDataSource(driver: String, url: String, username: String, password: String) : DataSource {
            log.info("Creating DataSource for url: $url with username $username and driver: $driver")
            val ds = HikariDataSource()
            ds.setDriverClassName(driver)
            ds.username = username
            ds.jdbcUrl = url
            ds.username = username
            ds.password = password
            return ds
        }

        private fun createTestDatasource() : DataSource {
            return createDataSource("org.h2.Driver", "jdbc:h2:mem:test", "sa", "")
        }
    }
}
