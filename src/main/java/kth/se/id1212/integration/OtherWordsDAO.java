package kth.se.id1212.integration;

import kth.se.id1212.model.LanguageBean;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;

/**
 * Class for handling communication with hur duthe database
 */
public class OtherWordsDAO {
    private Connection connection;
    private PreparedStatement findLanguages;
    private PreparedStatement findReports;
    private PreparedStatement findSettings;
    private PreparedStatement findUsers;
    private PreparedStatement findUser;
    private PreparedStatement findWords;

    private PreparedStatement createReport;
    private PreparedStatement updateSettings;
    private PreparedStatement createUser;
    private PreparedStatement createWord;
    private PreparedStatement updateWordCorrect;
    private PreparedStatement updateWordSkipped;
    private PreparedStatement updateUserGames;

    private PreparedStatement deleteUser;
    private PreparedStatement deleteWord;

    /**
     * Constructor
     */
    public OtherWordsDAO(){
        try{
            connectToDB();
        } catch (ClassNotFoundException | SQLException e){
            System.out.println("Couldn't connect to db, " + e.getMessage());
        }
    }

    public ArrayList<LanguageBean> findLanguages(){

    }

    private void connectToDB() throws ClassNotFoundException, SQLException {
        Class.forName("org.apache.derby.jdbc.ClientDriver");
        connection = DriverManager.getConnection("jdbc:derby://localhost:1527/maoDB",
                System.getenv("dbuser"), System.getenv("dbpassword"));
        connection.setAutoCommit(false);
    }

    private void prepareStatements() throws SQLException {
        findLanguages = connection.prepareStatement(
                "SELECT * FROM languages"
        );
        findReports = connection.prepareStatement(
                "SELECT * FROM reports"
        );
        findSettings = connection.prepareStatement(
                "SELECT * FROM user_settings WHERE CASE user_id = ?"
        );
        findUsers = connection.prepareStatement(
                "SELECT username, gamesplayed FROM users"
        );
        findUser = connection.prepareStatement(
                "SELECT id, username, admin, gamesPlayed FROM users " +
                        "WHERE username = ? AND password = ?"
        );
        findWords = connection.prepareStatement(
                "SELECT id, word, clue FROM words WHERE id in ?"
        );
        updateWordCorrect = connection.prepareStatement(
                "UPDATE words SET correctlyGuessed = correctlyGuessed +1 " +
                        "WHERE id = ?"
        );
        updateWordSkipped = connection.prepareStatement(
                "UPDATE words SET skipped = skipped +1 " +
                        "WHERE id = ?"
        );
        createReport = connection.prepareStatement(
                "INSERT INTO reports (timeOfReport, reportingUser, reportedWord, reason)" +
                        "VALUES (now(), ?, ?, ?)"
        );
        updateSettings = connection.prepareStatement(
                "UPDATE user_settings " +
                        "SET language_id = ?, secondsPerRound = ?, roundsPerGame = ?" +
                        "WHERE user_id = ?"
        );
        updateUserGames = connection.prepareStatement(
                "UPDATE user SET gamesPlayed = gamesPlayed + 1" +
                        "WHERE id = ?"
        );
        createUser = connection.prepareStatement(
                "INSERT INTO users (username, password, admin, gamesplayed)" +
                        "VALUES (?, ?, false, 0)"
        );
        createWord = connection.prepareStatement(
                "INSERT INTO words (word, clue, correctlyGuessed, skipped, flags)" +
                        "VALUES (?, ?, 0, 0, 0)"
        );
        deleteUser = connection.prepareStatement(
                "DELETE FROM users WHERE id = ?"
        );
        deleteWord = connection.prepareStatement(
                "DELETE FROM words WHERE id = ?"
        );

    }

}
