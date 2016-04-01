package no.lillehaug.landingsopplysninger.repository

import no.lillehaug.landingsopplysninger.api.LandingsopplysningerRepository
import no.lillehaug.landingsopplysninger.api.Leveringslinje
import no.lillehaug.landingsopplysninger.repository.database.Database
import org.slf4j.LoggerFactory
import java.sql.Date

class JdbcRepository(val database: Database) : LandingsopplysningerRepository {

    val log = LoggerFactory.getLogger(JdbcRepository::class.java)

    override fun alleLeveranselinjer(): List<Leveringslinje> {
        return database.readOnly {
            val ps = it.prepareStatement("select * from leveringslinje")
            val rs = ps.executeQuery()
            val results = mutableListOf<Leveringslinje>()
            while(rs.next()) {
                results.add(
                        Leveringslinje(
                                rs.getString("fartøy"),
                                rs.getDate("landingsdato").toLocalDate(),
                                rs.getString("mottak"),
                                rs.getString("fiskeslag"),
                                rs.getString("tilstand"),
                                rs.getString("størrelse"),
                                rs.getString("kvalitet"),
                                rs.getDouble("nettovekt")))
            }
            results.toList()
        }
    }

    override fun lagreLeveranselinjer(leveranselinjer: Collection<Leveringslinje>) {
        database.transactional {
            val ps = it.prepareStatement("insert into leveringslinje(fartøy, landingsdato, mottak, fiskeslag, tilstand, størrelse, kvalitet, nettovekt) values (?,?,?,?,?,?,?,?)")
            leveranselinjer.forEach {
                ps.setString(1, it.fartøy)
                ps.setDate(2, Date.valueOf(it.landingsdato))
                ps.setString(3, it.mottak)
                ps.setString(4, it.fiskeslag)
                ps.setString(5, it.tilstand)
                ps.setString(6, it.størrelse)
                ps.setString(7, it.kvalitet)
                ps.setDouble(8, it.nettovekt)
                ps.addBatch()
            }
            val numInserted = ps.executeBatch()
            log.info("Inserted {} rows", numInserted.size)
        }
    }
}
