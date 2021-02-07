package fr.isen.java2.db.daos;


import static org.assertj.core.api.Assertions.assertThat;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import fr.isen.java2.db.entities.Film;

public class FilmDaoTestCase {

	private FilmDao filmDao = new FilmDao();

	@Before
	public void initDb() throws Exception {
		Connection connection = DataSourceFactory.getDataSource().getConnection();
		Statement stmt = connection.createStatement();
		stmt.executeUpdate(
				"CREATE TABLE IF NOT EXISTS genre (idgenre INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT , name VARCHAR(50) NOT NULL);");
		stmt.executeUpdate(
				"CREATE TABLE IF NOT EXISTS film (\r\n"
				+ "  idfilm INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\r\n" + "  title VARCHAR(100) NOT NULL,\r\n"
				+ "  release_date DATETIME NULL,\r\n" + "  genre_id INT NOT NULL,\r\n" + "  duration INT NULL,\r\n"
				+ "  director VARCHAR(100) NOT NULL,\r\n" + "  summary MEDIUMTEXT NULL,\r\n"
				+ "  CONSTRAINT genre_fk FOREIGN KEY (genre_id) REFERENCES genre (idgenre));");
		stmt.executeUpdate("DELETE FROM film");
		stmt.executeUpdate("DELETE FROM genre");
		stmt.executeUpdate("INSERT INTO genre(idgenre,name) VALUES (1,'Drama')");
		stmt.executeUpdate("INSERT INTO genre(idgenre,name) VALUES (2,'Comedy')");
		stmt.executeUpdate("INSERT INTO film(idfilm,title, release_date, genre_id, duration, director, summary) "
				+ "VALUES (1, 'Title 1', '2015-11-26 12:00:00.000', 1, 120, 'director 1', 'summary of the first film')");
		stmt.executeUpdate("INSERT INTO film(idfilm,title, release_date, genre_id, duration, director, summary) "
				+ "VALUES (2, 'My Title 2', '2015-11-14 12:00:00.000', 2, 114, 'director 2', 'summary of the second film')");
		stmt.executeUpdate("INSERT INTO film(idfilm,title, release_date, genre_id, duration, director, summary) "
				+ "VALUES (3, 'Third title', '2015-12-12 12:00:00.000', 2, 176, 'director 3', 'summary of the third film')");
		stmt.close();
		connection.close();
	}
	
	 @Test
	 public void shouldListFilms() {
		 // WHEN
		List<Film> film = filmDao.listFilms();
		// THEN
		System.out.println(film.get(0));
	 }
	
	 @Test
	 public void shouldListFilmsByGenre() {
		List<Film> film = filmDao.listFilms();
		// THEN
		System.out.println(film.get(0));
		System.out.println(film.get(0).getGenre());

	 }
	
	 @Test
	 public void shouldAddFilm() throws Exception {
		 // WHEN 
		List<Film> film = filmDao.listFilms();
		Film film2 = film.get(0);
		film2.setTitle("Le film ajouté pour tester");
		filmDao.addFilm(film2);
		// THEN
		Connection connection = DataSourceFactory.getDataSource().getConnection();
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery("SELECT * FROM film WHERE title='Le film ajouté pour tester'");
		assertThat(resultSet.next()).isTrue();
		assertThat(resultSet.getInt("idfilm")).isNotNull();
		assertThat(resultSet.getString("title")).isEqualTo("Le film ajouté pour tester");
		assertThat(resultSet.next()).isFalse();
		resultSet.close();
		statement.close();
		connection.close();

	 }
}
