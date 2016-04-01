package no.lillehaug.landingsopplysninger.repository

import no.lillehaug.landingsopplysninger.api.Leveringslinje
import no.lillehaug.landingsopplysninger.repository.database.Database
import org.junit.Before
import kotlin.test.assertEquals
import org.junit.Test
import java.time.LocalDate

class JdbcRepositoryTest {
    val database = Database.createTestDatabase()
    val repository = JdbcRepository(database)

    @Before fun setup() {
        database.clean()
        database.migrate()
    }

    @Test fun insertAndGetAll() {
        val testData = listOf(
                Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 29), "Steigen Sjømat As (N869)", "Hyse", "Sluh", "-0,8 Kg", "A", 1.0),
                Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 29), "Steigen Sjømat As (N869)", "Kveite", "Sluh", "-20 Kg", "A", 6.5),
                Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 29), "Steigen Sjømat As (N869)", "Kvitlange", "Sluh", "2,0+ Kg", "A", 26.0),
                Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 29), "Steigen Sjømat As (N869)", "Lyr", "Sluh", "2,0+ Kg", "A", 12.0),
                Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 29), "Steigen Sjømat As (N869)", "Sei", "Sluh", "2,3+ Kg", "A", 3.0),
                Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 29), "Steigen Sjømat As (N869)", "Torsk", "Sluh", "6,0+ Kg", "A", 539.0),
                Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 29), "Steigen Sjømat As (N869)", "Torsk", "Sluh", "2,5+ Kg", "A", 1049.0),
                Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 28), "Steigen Sjømat As (N869)", "Kvitlange", "Sluh", "2,0+ Kg", "A", 20.0),
                Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 28), "Steigen Sjømat As (N869)", "Lyr", "Sluh", "2,0+ Kg", "A", 11.0),
                Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 28), "Steigen Sjømat As (N869)", "Sei", "Sluh", "1,2-2,3 Kg", "A", 4.0),
                Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 28), "Steigen Sjømat As (N869)", "Torsk", "Sluh", "6,0+ Kg", "A", 678.0),
                Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 28), "Steigen Sjømat As (N869)", "Torsk", "Sluh", "2,5+ Kg", "Skadd", 50.0),
                Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 28), "Steigen Sjømat As (N869)", "Torsk", "Sluh", "2,5+ Kg", "A", 1545.0),
                Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 22), "Steigen Sjømat As (N869)", "Gråstbit", "Sluh", "1,0+ Kg", "A", 4.0),
                Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 22), "Steigen Sjømat As (N869)", "Hyse", "Sluh", "-0,8 Kg", "A", 6.5),
                Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 22), "Steigen Sjømat As (N869)", "Lyr", "Sluh", "2,0+ Kg", "A", 3.0),
                Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 22), "Steigen Sjømat As (N869)", "Sei", "Sluh", "2,3+ Kg", "A", 5.0),
                Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 22), "Steigen Sjømat As (N869)", "Sei", "Sluh", "1,2-2,3 Kg", "A", 5.0),
                Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 22), "Steigen Sjømat As (N869)", "Torsk", "Sluh", "2,5+ Kg", "A", 260.0),
                Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 22), "Steigen Sjømat As (N869)", "Torsk", "Sluh", "6,0+ Kg", "A", 225.0),
                Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 22), "Steigen Sjømat As (N869)", "Torsk", "Sluh", "1,0-2,5 Kg", "A", 10.0),
                Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 22), "Steigen Sjømat As (N869)", "Torsk", "Rogn", "Unspec", "Skadd", 60.0),
                Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 21), "Steigen Sjømat As (N869)", "Hyse", "Sluh", "0,8+ Kg", "A", 8.0),
                Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 21), "Steigen Sjømat As (N869)", "Kvitlange", "Sluh", "2,0+ Kg", "A", 2.0),
                Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 21), "Steigen Sjømat As (N869)", "Sei", "Sluh", "2,3+ Kg", "A", 10.0),
                Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 21), "Steigen Sjømat As (N869)", "Torsk", "Sluh", "1,0-2,5 Kg", "A", 20.0),
                Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 21), "Steigen Sjømat As (N869)", "Torsk", "Sluh", "2,5+ Kg", "A", 361.0),
                Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 21), "Steigen Sjømat As (N869)", "Torsk", "Rogn", "Unspec", "A", 64.0),
                Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 21), "Steigen Sjømat As (N869)", "Torsk", "Sluh", "6,0+ Kg", "A", 216.0),
                Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 20), "Steigen Sjømat As (N869)", "Hyse", "Sluh", "0,8+ Kg", "A", 3.0),
                Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 20), "Steigen Sjømat As (N869)", "Kveite", "Sluh", "-20 Kg", "A", 6.0),
                Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 20), "Steigen Sjømat As (N869)", "Kvitlange", "Sluh", "2,0+ Kg", "A", 3.0),
                Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 20), "Steigen Sjømat As (N869)", "Sei", "Sluh", "1,2-2,3 Kg", "A", 15.0),
                Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 20), "Steigen Sjømat As (N869)", "Sei", "Sluh", "2,3+ Kg", "A", 20.0),
                Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 20), "Steigen Sjømat As (N869)", "Torsk", "Sluh", "2,5+ Kg", "A", 600.0),
                Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 20), "Steigen Sjømat As (N869)", "Torsk", "Rogn", "Unspec", "A", 89.5),
                Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 20), "Steigen Sjømat As (N869)", "Torsk", "Sluh", "6,0+ Kg", "A", 393.5),
                Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 20), "Steigen Sjømat As (N869)", "Torsk", "Sluh", "1,0-2,5 Kg", "A", 24.0),
                Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 20), "Steigen Sjømat As (N869)", "Torsk", "Sluh", "2,5+ Kg", "Skadd", 5.0),
                Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 19), "Steigen Sjømat As (N869)", "Hyse", "Sluh", "-0,8 Kg", "A", 8.0),
                Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 19), "Steigen Sjømat As (N869)", "Kveite", "Sluh", "-20 Kg", "A", 13.0),
                Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 19), "Steigen Sjømat As (N869)", "Kvitlange", "Sluh", "2,0+ Kg", "A", 10.0),
                Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 19), "Steigen Sjømat As (N869)", "Sei", "Sluh", "1,2-2,3 Kg", "A", 8.0),
                Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 19), "Steigen Sjømat As (N869)", "Sei", "Sluh", "2,3+ Kg", "A", 25.0),
                Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 19), "Steigen Sjømat As (N869)", "Torsk", "Rogn", "Unspec", "Skadd", 45.0),
                Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 19), "Steigen Sjømat As (N869)", "Torsk", "Sluh", "6,0+ Kg", "A", 214.0),
                Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 19), "Steigen Sjømat As (N869)", "Torsk", "Sluh", "1,0-2,5 Kg", "A", 24.5),
                Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 19), "Steigen Sjømat As (N869)", "Torsk", "Sluh", "2,5+ Kg", "A", 280.0),
                Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 18), "Steigen Sjømat As (N869)", "Brosme", "Sluh", "1,0-2,0 Kg", "A", 3.0),
                Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 18), "Steigen Sjømat As (N869)", "Hyse", "Sluh", "-0,8 Kg", "A", 4.0),
                Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 18), "Steigen Sjømat As (N869)", "Torsk", "Sluh", "6,0+ Kg", "A", 73.0),
                Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 18), "Steigen Sjømat As (N869)", "Torsk", "Sluh", "2,5+ Kg", "A", 173.0),
                Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 18), "Steigen Sjømat As (N869)", "Torsk", "Rogn", "Unspec", "Skadd", 27.0),
                Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 17), "Steigen Sjømat As (N869)", "Hyse", "Sluh", "0,8+ Kg", "A", 6.0),
                Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 17), "Steigen Sjømat As (N869)", "Hyse", "Sluh", "-0,8 Kg", "A", 3.0),
                Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 17), "Steigen Sjømat As (N869)", "Kveite", "Sluh", "-20 Kg", "A", 14.0),
                Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 17), "Steigen Sjømat As (N869)", "Kvitlange", "Sluh", "0,7-2 Kg", "A", 1.0),
                Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 17), "Steigen Sjømat As (N869)", "Sei", "Sluh", "2,3+ Kg", "A", 10.0),
                Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 17), "Steigen Sjømat As (N869)", "Torsk", "Rogn", "Unspec", "A", 27.0),
                Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 17), "Steigen Sjømat As (N869)", "Torsk", "Sluh", "2,5+ Kg", "A", 275.0),
                Leveringslinje("N 0027SG", LocalDate.of(2016, 3, 17), "Steigen Sjømat As (N869)", "Torsk", "Sluh", "6,0+ Kg", "A", 153.0)
        )
        repository.lagreLeveranselinjer(testData)
        val retrievedData = repository.alleLeveranselinjer()
        assertEquals(testData, retrievedData)
        val maxLandingsdato = testData.map { it.landingsdato }.max()
        assertEquals(maxLandingsdato, repository.forrigeLandingFor("N 0027SG"))
    }
}
