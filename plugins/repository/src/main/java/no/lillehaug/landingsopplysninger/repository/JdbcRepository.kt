package no.lillehaug.landingsopplysninger.repository

import no.lillehaug.landingsopplysninger.api.LandingsdataQuery
import no.lillehaug.landingsopplysninger.api.LandingsopplysningerRepository
import no.lillehaug.landingsopplysninger.api.Leveringslinje
import no.lillehaug.landingsopplysninger.api.LeveringslinjeWithId
import no.lillehaug.landingsopplysninger.library.TryWR.Companion.trywr
import no.lillehaug.landingsopplysninger.repository.database.Database
import org.slf4j.LoggerFactory
import java.sql.Date
import java.time.LocalDate
import java.util.*

class JdbcRepository(val database: Database) : LandingsopplysningerRepository {
    val log = LoggerFactory.getLogger(JdbcRepository::class.java)

    override fun alleLeveranselinjer(): List<LeveringslinjeWithId> {
        return this.alleLeveranselinjer(no.lillehaug.landingsopplysninger.api.LandingsdataQuery(null, null))
    }

    private fun getParams(landingsdataQuery: LandingsdataQuery) : List<Any> {
        val params = mutableListOf<Any>()
        if(landingsdataQuery.fraDato != null){
            params.add(Date.valueOf(landingsdataQuery.fraDato))
        }
        if(landingsdataQuery.tilDato != null){
            params.add(Date.valueOf(landingsdataQuery.tilDato))
        }

        return params
    }

    private fun getWhere(landingsdataQuery: LandingsdataQuery) : String {
        if(landingsdataQuery.tilDato == null && landingsdataQuery.fraDato == null) {
            return ""
        }

        val parts = mutableListOf<String>()
        if(landingsdataQuery.fraDato != null) {
            parts.add(" landingsdato >= ?")
        }
        if(landingsdataQuery.tilDato != null) {
            parts.add(" landingsdato <= ?")
        }
        return parts.joinToString(" AND ", "WHERE ")
    }

    override fun alleLeveranselinjer(landingsdataQuery: LandingsdataQuery): List<LeveringslinjeWithId> {
        val where = getWhere(landingsdataQuery)
        val params = getParams(landingsdataQuery)
        return database.readOnly {
            val ps = it.prepareStatement("select * from leveringslinje ${where} order by landingsdato desc,fiskeslag,kvalitet")
            var i = 1
            for (param in params) {
                ps.setObject(i++, param)
            }
            val rs = ps.executeQuery()
            trywr(rs) {
                val results = mutableListOf<LeveringslinjeWithId>()
                while (rs.next()) {
                    results.add(
                            LeveringslinjeWithId(
                                    rs.getObject("id", UUID::class.java),
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
    }

    override fun lagreLeveranselinjer(leveranselinjer: Collection<Leveringslinje>) {
        if (leveranselinjer.isNotEmpty()) {
            database.transactional {
                val ps = it.prepareStatement("insert into leveringslinje(fartøy, landingsdato, mottak, fiskeslag, tilstand, størrelse, kvalitet, nettovekt) values (?,?,?,?,?,?,?,?)")
                trywr(ps){
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
    }

    override fun forrigeLandingFor(registration: String): LocalDate {
        return database.readOnly {
            val ps = it.prepareStatement("select max(landingsdato) as landingsdato from leveringslinje where fartøy = ?")
            trywr(ps){
                ps.setString(1, registration)
                val rs = ps.executeQuery()
                trywr(rs){
                    if(rs.next()) {
                        rs.getDate("landingsdato")?.toLocalDate() ?: LocalDate.MIN
                    } else {
                        LocalDate.MIN
                    }
                }
            }
        }
    }
}
