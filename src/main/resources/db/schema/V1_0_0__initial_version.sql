-- fartøy: String, val landingsdato: LocalDate, val mottak: String, val fiskeslag: String, val tilstand: String, val størrelse: String, val kvalitet: String, val nettovekt: Double
CREATE TABLE leveringslinje (
  fartøy varchar(255) NOT NULL,
  landingsdato datetime NOT NULL,
  mottak varchar(255) NOT NULL,
  fiskeslag varchar(255) NOT NULL,
  tilstand varchar(255) NOT NULL,
  størrelse varchar(255) NOT NULL,
  kvalitet varchar(255) NOT NULL,
  nettovekt DECIMAL NOT NULL
);

