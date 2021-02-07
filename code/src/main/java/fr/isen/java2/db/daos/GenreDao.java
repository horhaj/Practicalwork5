package fr.isen.java2.db.daos;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fr.isen.java2.db.entities.Genre;

public class GenreDao {

	public List<Genre> listGenres() {

		List<Genre> listGenre = new ArrayList<>();
		try (Connection connection = DataSourceFactory.getDataSource().getConnection()) {
			try (Statement statement = connection.createStatement()) {
				try (ResultSet results = statement.executeQuery("select * from genre")) {
					while (results.next()) {
						Genre genre = new Genre(results.getInt("idgenre"),
												results.getString("name"));
						listGenre.add(genre);
					}
					results.close();
					statement.close();
					connection.close();
				}
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return listGenre;
		//throw new RuntimeException("Method is not yet implemented");

	}

	public Genre getGenre(String name) {
		try (Connection connection = DataSourceFactory.getDataSource().getConnection()) {
			try (PreparedStatement statement = connection.prepareStatement("select * from genre where name=?")) {
				statement.setString(1, name);
				try (ResultSet results = statement.executeQuery()) {
					if (results.next()) {
						Genre genre = new Genre(results.getInt("idgenre"),
												results.getString("name"));
						return genre;
					}
					results.close();
					statement.close();
					connection.close();
				}
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return null;
		//throw new RuntimeException("Method is not yet implemented");
	}

	public void addGenre(String name) {
		try (Connection connection = DataSourceFactory.getDataSource().getConnection()) {
			String sqlQuery = "insert into genre(name) " + "VALUES(?)";
			try (PreparedStatement statement = connection.prepareStatement(
							sqlQuery, Statement.RETURN_GENERATED_KEYS)) {
				statement.setString(1, name);
				statement.executeUpdate();
				statement.close();
			}
			connection.close();
		}catch (SQLException e) {
			// Manage Exception
			e.printStackTrace();
		}
		//throw new RuntimeException("Method is not yet implemented");
	}
}
