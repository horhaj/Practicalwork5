package fr.isen.java2.db.daos;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fr.isen.java2.db.entities.Film;
import fr.isen.java2.db.entities.Genre;

public class FilmDao {

	public List<Film> listFilms() {
		List<Film> listFilm = new ArrayList<>();
		try (Connection connection = DataSourceFactory.getDataSource().getConnection()) {
			try (Statement statement = connection.createStatement()) {
				try (ResultSet results = statement.executeQuery("SELECT * FROM film JOIN genre ON film.genre_id = genre.idgenre")) {
					while (results.next()) {
						Genre genre = new Genre(results.getInt("idgenre"),
												results.getString("name"));
						Film film = new Film(results.getInt("idfilm"),
												results.getString("title"),
												results.getDate("release_date").toLocalDate(),
												genre,
												results.getInt("duration"),
												results.getString("director"),
												results.getString("summary"));
						listFilm.add(film);
					}
					results.close();
					statement.close();
					connection.close();
				}
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}
		
		return listFilm;
		//throw new RuntimeException("Method is not yet implemented");
	}

	// la methode était listFilmByFilm, or dans le practical work elle est nommé listFilmsByGenre
	// je l'ai donc changé et adapté

	public List<Film> listFilmsByGenre(String GenreName) {
		List<Film> listFilm = new ArrayList<>();
		try (Connection connection = DataSourceFactory.getDataSource().getConnection()) {
			try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM film JOIN genre ON film.genre_id = genre.idgenre WHERE genre.name = ?")) {
				statement.setString(1, GenreName);
				try (ResultSet results = statement.executeQuery()) {
					while (results.next()) {
						Genre genre = new Genre(results.getInt("idgenre"),
												results.getString("name"));
						Film film = new Film(results.getInt("idfilm"),
												results.getString("title"),
												results.getDate("release_date").toLocalDate(),
												genre,
												results.getInt("duration"),
												results.getString("director"),
												results.getString("summary"));
						listFilm.add(film);
					}
					results.close();
					statement.close();
					connection.close();
				}
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return listFilm;
		//throw new RuntimeException("Method is not yet implemented");
	}

	public Film addFilm(Film film) {
		try (Connection connection = DataSourceFactory.getDataSource().getConnection()) {
			String sqlQuery = "INSERT INTO film(title,release_date,genre_id,duration,director,summary)" + "VALUES(?,?,?,?,?,?)";
			try (PreparedStatement statement = connection.prepareStatement(
							sqlQuery, Statement.RETURN_GENERATED_KEYS)) {
				statement.setString(1, film.getTitle());
				statement.setDate(2, Date.valueOf(film.getReleaseDate()));
				statement.setInt(3, film.getGenre().getId());
				statement.setInt(4, film.getDuration());
				statement.setString(5, film.getDirector());
				statement.setString(6, film.getSummary());
				statement.executeUpdate();
				connection.close();
			}
		}catch (SQLException e) {
			// Manage Exception
			e.printStackTrace();
		}
		try (Connection connection2 = DataSourceFactory.getDataSource().getConnection()) {
			try (PreparedStatement statement2 = connection2.prepareStatement("select * from film where title=?" + 
														" and release_date=? and genre_id=? and duration=?" +
														"and director=? and summary=?")) {
			statement2.setString(1, film.getTitle());
			statement2.setDate(2, Date.valueOf(film.getReleaseDate()));
			statement2.setInt(3, film.getGenre().getId());
			statement2.setInt(4, film.getDuration());
			statement2.setString(5, film.getDirector());
			statement2.setString(6, film.getSummary());
			statement2.executeUpdate();
			try (ResultSet results2 = statement2.executeQuery()) {
				if (results2.next()) {
					Film film2 = new Film(results2.getInt("idfilm"), results2.getString("title"),
					results2.getDate("release_date").toLocalDate(), film.getGenre(),
					 results2.getInt("duration"), results2.getString("director"), results2.getString("summary"));
					return film2;
					
				}
				results2.close();
				statement2.close();
				connection2.close();
			}
		}
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return null;
		//throw new RuntimeException("Method is not yet implemented");
	}
}
