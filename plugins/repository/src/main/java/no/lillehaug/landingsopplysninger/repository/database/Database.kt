package no.lillehaug.landingsopplysninger.repository.database

import com.codahale.metrics.MetricRegistry
import com.codahale.metrics.health.HealthCheckRegistry
import com.zaxxer.hikari.HikariDataSource
import no.lillehaug.landingsopplysninger.repository.jdbc.JDBC
import org.flywaydb.core.Flyway
import org.slf4j.LoggerFactory
import java.sql.Connection
import javax.sql.DataSource

class Database(val datasource : HikariDataSource) {
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

    fun close() {
        datasource.close()
    }

    companion object {
        val log = LoggerFactory.getLogger(Database::class.java)

        fun createDatabase(driver: String, url: String, username: String, password: String, healthCheckRegistry: HealthCheckRegistry, metricRegistry: MetricRegistry) : Database {
            return Database(createDataSource(driver, url, username, password, healthCheckRegistry, metricRegistry))
        }

        fun createTestDatabase () : Database {
            return Database(createTestDatasource())
        }

        private fun createDataSource(driver: String, url: String, username: String, password: String, healthCheckRegistry: HealthCheckRegistry?, metricRegistry: MetricRegistry?) : HikariDataSource {
            log.info("Creating DataSource for url: $url with username $username and driver: $driver")
            val ds = HikariDataSource()
            ds.setDriverClassName(driver)
            ds.username = username
            ds.jdbcUrl = url
            ds.username = username
            ds.password = password

            if (healthCheckRegistry != null) {
                ds.healthCheckRegistry = healthCheckRegistry
            }
            if(metricRegistry != null) {
                ds.metricRegistry = metricRegistry
            }
            return ds
        }

        private fun createTestDatasource() : HikariDataSource {
            return createDataSource("org.h2.Driver", "jdbc:h2:mem:test", "sa", "", null, null)
        }
    }
}
