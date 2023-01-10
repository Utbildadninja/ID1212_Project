package kth.se.id1212.integration;

import kth.se.id1212.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.Set;

/**
 * Class for handling communication with hur duthe database
 */
public class OtherWordsDAO {
    private Connection connection;
    private PreparedStatement findLanguagesStmt;
    private PreparedStatement findReportsStmt;
    private PreparedStatement findSettingsStmt;
    private PreparedStatement findUsersStmt;
    private PreparedStatement findUserStmt;
    private PreparedStatement findNoOfWordsStmt;
    private PreparedStatement findWordsStmt;

    private PreparedStatement createReportStmt;
    private PreparedStatement updateSettingsStmt;
    private PreparedStatement createUserStmt;
    private PreparedStatement createWordStmt;
    private PreparedStatement updateWordCorrectStmt;
    private PreparedStatement updateWordSkippedStmt;
    private PreparedStatement updateUserGamesStmt;

    private PreparedStatement deleteUserStmt;
    private PreparedStatement deleteWordStmt;

    /**
     * Constructor
     */
    public OtherWordsDAO(){
        try{
            connectToDB();
            prepareStatements();
        } catch (ClassNotFoundException | SQLException e){
            e.printStackTrace();
            System.out.println("Couldn't connect to db, " + e.getMessage());
        }
    }

    /**
     * Find the available languages to play the game in
     * @return list of language beans, one per available language (ID, languagename)
     */
    public ArrayList<LanguageBean> findLanguages(){
        ArrayList<LanguageBean> languages = new ArrayList<>();
        ResultSet resultSet = null;
        try {
            resultSet = findLanguagesStmt.executeQuery();
            while (resultSet.next()){
                languages.add(new LanguageBean(resultSet.getInt(1), resultSet.getString(2)));
            }
            connection.commit();
        } catch (SQLException e) {
            exceptionHandler("Couldn't execute query: ", e);
            throw new RuntimeException(e);
        } finally {
            closeResult(resultSet);
        }
        return languages;
    }

    /**
     * Find the user reports about inappropriate words
     * @return list of ReportBeans, one per user report
     */
    public ArrayList<ReportBean> findReports(){
        ArrayList<ReportBean> reports = new ArrayList<>();
        ResultSet resultSet = null;
        try {
            resultSet = findReportsStmt.executeQuery();
            while (resultSet.next()){
                reports.add(new ReportBean());  //TODO add properties to the ReportBeans
            }
            connection.commit();
        } catch (SQLException e) {
            exceptionHandler("Couldn't execute reports query: ", e);
            throw new RuntimeException(e);
        } finally {
            closeResult(resultSet);
        }
        return reports;
    }

    /**
     * Takes UserID and find what settings that user has saved in the database
     * //TODO could be switched to username? since we might want to fetch the settings on login
     * @param userID the ID of the logged-in user
     * @return list of
     */
    public SettingsBean findUserSettings(int userID){
        SettingsBean userSettings = null;
        ResultSet resultSet = null;
        try {
            findSettingsStmt.setInt(1, userID);
            resultSet = findSettingsStmt.executeQuery();
            while (resultSet.next()){
                userSettings = new SettingsBean(
                        resultSet.getInt("language_id"), resultSet.getInt("secondsPerRound"),
                        resultSet.getInt("roundsPerGame")
                );
            }
            connection.commit();
        } catch (SQLException e) {
            exceptionHandler("Couldn't execute settings query: ", e);
            throw new RuntimeException(e);
        } finally {
            closeResult(resultSet);
        }
        return userSettings;
    }

    /**
     * Find all users, ordered by number of games played (to display "most frequent players" highscore)
     * @return list of UserBeans
     */
    public ArrayList<UserBean> findAllUsersByPlayedGames(){
        ArrayList<UserBean> users = new ArrayList<>();
        ResultSet resultSet = null;
        try {
            resultSet = findUsersStmt.executeQuery();
            while (resultSet.next()){
                users.add(new UserBean(
                        resultSet.getInt("id"), resultSet.getString("username"),
                        resultSet.getBoolean("admin"), resultSet.getInt("gamesPlayed")
                ));
            }
            connection.commit();
        } catch (SQLException e) {
            exceptionHandler("Couldn't execute list users query: ", e);
            throw new RuntimeException(e);
        } finally {
            closeResult(resultSet);
        }
        return users;
    }

    /**
     * Called with login request, tries to match entered credentials with user in the db
     * @param username attempted login username
     * @param password attempted login password
     * @return UserBean if user is found in db, null if credentials not matching
     */
    public UserBean findUser(String username, String password){
        ResultSet resultSet = null;
        UserBean user = null;
        int resultSize = 0;
        int id = 0;
        try {
            //set the params for the PreparedStatement
            findUserStmt.setString(1, username);
            findUserStmt.setString(2, password);

            resultSet = findUserStmt.executeQuery();

            //TODO test if this version works
            if(resultSet.next()){
                user = new UserBean(resultSet.getInt("id"), resultSet.getString("username"),
                        resultSet.getBoolean("admin"), resultSet.getInt("gamesPlayed")
                );
                System.out.println(user.getID() + user.getUsername());
            }
            connection.commit();
        } catch (Exception e) {
            exceptionHandler("Couldn't execute match user (login) query: ", e);
        } finally { //always close the result set
            closeResult(resultSet);
        }

        return user;            //TODO give some kind of feedback to user if this is null (do in higher layer)
    }

    /**
     * Will return noOfWords words from the database based on randomised wordIDs..
     * TODO a bit fishy not sure it will work, problematic if we have removed words etc,
     * ie some IDs are missing from the DB...
     * @param noOfWords
     * @param languageID
     * @return
     */
    public ArrayList<WordBean> words(int noOfWords, int languageID){
        ArrayList<WordBean> words = new ArrayList<>();
        ResultSet resultSetAmount = null;
        ResultSet resultSetWords = null;
        try{
            resultSetAmount = findNoOfWordsStmt.executeQuery();
            //int[] of size noOfWords, with random values(1, resultSetAmount) //very problematic if there are removed words
            //convert to String
            //TODO test if idea works at all
//            findWordsStmt.setString("(1,2,3)");
            resultSetWords = findWordsStmt.executeQuery();
            //while hasNext add to words
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return words;
    }

    private void connectToDB() throws ClassNotFoundException, SQLException {
        Class.forName("org.apache.derby.jdbc.ClientDriver");
        connection = DriverManager.getConnection("jdbc:derby://localhost:1527/maoDB",
                System.getenv("dbuser"), System.getenv("dbpassword"));
        connection.setAutoCommit(false);
    }

    private void prepareStatements() throws SQLException {
//        findLanguagesStmt = connection.prepareStatement(
//                "SELECT * FROM languages"
//        );
//        findReportsStmt = connection.prepareStatement(
//                "SELECT * FROM reports"
//        );
        findSettingsStmt = connection.prepareStatement(                 //TODO join with table languages to get name
                "SELECT * FROM user_settings WHERE CASE user_id = ?"
        );
//        findUsersStmt = connection.prepareStatement(
//                "SELECT username, gamesPlayed FROM users ORDER BY gamesPlayed"
//        );
        findUserStmt = connection.prepareStatement(
                "SELECT id, username, admin, gamesPlayed FROM users " +
                        "WHERE username = ? AND password = ?"
        );
//        findNoOfWordsStmt = connection.prepareStatement(        //Could theoretically yield problems if someone
//                "SELECT COUNT(*) FROM words"
//        );
//        findWordsStmt = connection.prepareStatement(            //TODO take language as parameter
//                "SELECT id, word, clue FROM words WHERE id in ?"
//        );
//        updateWordCorrectStmt = connection.prepareStatement(
//                "UPDATE words SET correctlyGuessed = correctlyGuessed +1 " +
//                        "WHERE id = ?"
//        );
//        updateWordSkippedStmt = connection.prepareStatement(
//                "UPDATE words SET skipped = skipped +1 " +
//                        "WHERE id = ?"
//        );
//        createReportStmt = connection.prepareStatement(
//                "INSERT INTO reports (timeOfReport, reportingUser, reportedWord, reason)" +
//                        "VALUES (now(), ?, ?, ?)"
//        );
//        updateSettingsStmt = connection.prepareStatement(
//                "UPDATE user_settings " +
//                        "SET language_id = ?, secondsPerRound = ?, roundsPerGame = ?" +
//                        "WHERE user_id = ?"
//        );
//        updateUserGamesStmt = connection.prepareStatement(
//                "UPDATE users SET gamesPlayed = gamesPlayed + 1" +
//                        "WHERE id = ?"
//        );
//        createUserStmt = connection.prepareStatement(
//                "INSERT INTO users (username, password, admin, gamesplayed)" +
//                        "VALUES (?, ?, false, 0)"
//        );
//        createWordStmt = connection.prepareStatement(
//                "INSERT INTO words (word, clue, correctlyGuessed, skipped, flags)" +
//                        "VALUES (?, ?, 0, 0, 0)"
//        );
//        deleteUserStmt = connection.prepareStatement(
//                "DELETE FROM users WHERE id = ?"
//        );
//        deleteWordStmt = connection.prepareStatement(
//                "DELETE FROM words WHERE id = ?"
//        );
    }

    private void closeResult(ResultSet result){
        try {
            result.close();
        } catch (SQLException e) {
            System.out.println("Could not close result");
            throw new RuntimeException(e);
        }
    }

    private void exceptionHandler(String failMsg, Exception exception){
        System.out.println(failMsg + exception.getMessage());
        try{
            connection.rollback();
        } catch (SQLException e) {
            System.out.println("Could not rollback: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
