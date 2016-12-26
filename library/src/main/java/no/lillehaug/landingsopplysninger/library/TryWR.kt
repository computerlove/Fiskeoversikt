package no.lillehaug.landingsopplysninger.library

import org.slf4j.LoggerFactory

class TryWR {

    companion object {
        val log = LoggerFactory.getLogger(TryWR::class.java)

        inline fun <T : AutoCloseable, R> trywr(closeable: T, block: (T) -> R): R {
            try {
                return block(closeable);
            } catch (e: Exception) {
                log.error("Error", e)
                throw e
            } finally {
                closeable.close()
            }
        }
    }
}