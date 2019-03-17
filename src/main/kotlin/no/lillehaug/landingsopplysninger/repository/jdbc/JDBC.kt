package no.lillehaug.landingsopplysninger.repository.jdbc

import org.slf4j.LoggerFactory
import java.sql.Connection
import javax.sql.DataSource

class JDBC {
    companion object {
        val log = LoggerFactory.getLogger(JDBC::class.java)

        @JvmStatic
        fun <T> readOnly(ds: DataSource, action: (c: Connection) -> T): T {
            val connection = ds.connection
            val isReadOnly = connection.isReadOnly
            connection.isReadOnly = true
            return connection.use {
                val result = action(connection)
                connection.isReadOnly = isReadOnly
                result
            }
        }

        @JvmStatic
        fun <T> transactional(ds: DataSource, action: (c: Connection) -> T): T {
            val connection = ds.connection
            connection.autoCommit = false
            return connection.use {
                try {
                    val result = action(connection)
                    connection.commit()
                    result
                } catch(e: Exception) {
                    log.error("Exception in transaction", e)
                    connection.rollback()
                    throw e
                }
            }
        }


    }
}
