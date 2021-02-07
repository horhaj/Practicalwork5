package fr.isen.java2.db.daos;

import java.util.Scanner;

public class Testrunner {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("1 pour GenreTest, 2 pour FilmTest");
        int nbre = sc.nextInt();
        switch (nbre) {
            case 1:
                GenreDaoTestCase test = new GenreDaoTestCase();
                try {
                    test.initDatabase();
                    System.out.println("DataBase initiated");
                    test.shouldListGenres();
                    System.out.println("Genre listed");
                    test.shouldGetGenreByName();
                    System.out.println("Genre by Name gotten");
                    test.shouldNotGetUnknownGenre();
                    System.out.println("No Unknown genre gotten");
                    test.shouldAddGenre();
                    System.out.println("Genre added");
                } catch (Exception e) {
                    System.out.println("erreur");
                    e.printStackTrace();
                }
                break;
            case 2:
                FilmDaoTestCase test2 = new FilmDaoTestCase();
                try {
                    test2.initDb();
                    System.out.println("DataBase initiated");
                    test2.shouldListFilms();
                    System.out.println("film listed");
                    test2.shouldListFilmsByGenre();
                    System.out.println("Film listed by Genre");
                    test2.shouldAddFilm();
                    System.out.println("Film added");
                } catch (Exception e) {
                    System.out.println("erreur");
                    e.printStackTrace();
                }
            default:
                break;
        }
        sc.close();
        
    }
}
