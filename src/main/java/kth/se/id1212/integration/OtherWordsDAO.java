package kth.se.id1212.integration;

import kth.se.id1212.model.*;

import java.sql.*;
import java.util.ArrayList;


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
    private PreparedStatement findRandomWordStmt;

    private PreparedStatement createReportStmt;
    private PreparedStatement updateSettingsStmt;
    private PreparedStatement createUserStmt;
    private PreparedStatement createWordStmt;
    private PreparedStatement createSettingsStmt;
    private PreparedStatement updateWordCorrectStmt;
    private PreparedStatement updateWordSkippedStmt;
    private PreparedStatement updateUserGamesStmt;

    private PreparedStatement deleteUserStmt;
    private PreparedStatement deleteWordStmt;

    /**
     * Constructor
     */
    public OtherWordsDAO() {
        try {
            connectToDB();
            prepareStatements();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            System.out.println("Couldn't connect to db, " + e.getMessage());
        }
    }

    /**
     * Find the available languages to play the game in
     *
     * @return list of language beans, one per available language (ID, languagename)
     */
    public ArrayList<LanguageBean> findLanguages() {
        ArrayList<LanguageBean> languages = new ArrayList<>();
        ResultSet resultSet = null;
        try {
            resultSet = findLanguagesStmt.executeQuery();
            while (resultSet.next()) {
                languages.add(new LanguageBean(resultSet.getInt(1), resultSet.getString(2)));
            }
            connection.commit();
        } catch (SQLException e) {
            exceptionHandler("Couldn't execute query: ", e);
        } finally {
            closeResult(resultSet);
        }
        return languages;
    }

    /**
     * Finds the number of words in the db for certain language
     *
     * @param languageID the chosen language
     * @return number of words
     */
    public int findNoOfWords(int languageID) {
        int noOfWords = 0;
        ResultSet resultSet = null;
        try {
            findNoOfWordsStmt.setInt(1, languageID);
            resultSet = findNoOfWordsStmt.executeQuery();
            if (resultSet.next()) {
                noOfWords = resultSet.getInt(1);
            }
            connection.commit();
        } catch (SQLException e) {
            exceptionHandler("Couldn't execute query: ", e);
        } finally {
            closeResult(resultSet);
        }
        return noOfWords;
    }

    /**
     * Find the user reports about inappropriate words
     *
     * @return list of ReportBeans, one per user report
     */
    public ArrayList<ReportBean> findReports() {
        ArrayList<ReportBean> reports = new ArrayList<>();
        ResultSet resultSet = null;
        try {
            resultSet = findReportsStmt.executeQuery();
            while (resultSet.next()) {
                reports.add(new ReportBean());  //TODO add properties to the ReportBeans
            }
            connection.commit();
        } catch (SQLException e) {
            exceptionHandler("Couldn't execute reports query: ", e);
            e.printStackTrace();
        } finally {
            closeResult(resultSet);
        }
        return reports;
    }

    /**
     * Takes UserID and find what settings that user has saved in the database
     *
     * @param userID the ID of the logged-in user
     * @return list of
     */
    public SettingsBean findUserSettings(int userID) {
        SettingsBean userSettings = null;
        ResultSet resultSet = null;
        try {
            findSettingsStmt.setInt(1, userID);
            resultSet = findSettingsStmt.executeQuery();
            if (resultSet.next()) {
                userSettings = new SettingsBean(
                        resultSet.getInt("language_id"), resultSet.getString("languagename"), resultSet.getInt("secondsPerRound"),
                        resultSet.getInt("roundsPerGame")
                );
            }
            connection.commit();
        } catch (SQLException e) {
            exceptionHandler("Couldn't execute settings query: ", e);
        } finally {
            closeResult(resultSet);
        }
        System.out.println("current/new settings for user " + userID + ": " +
                userSettings.getLanguageID() + " " + userSettings.getLanguageName());
        return userSettings;
    }

    /**
     * Find all users, ordered by number of games played (to display "most frequent players" highscore)
     *
     * @return list of UserBeans
     */
    public ArrayList<UserBean> findAllUsersByPlayedGames() {
        ArrayList<UserBean> users = new ArrayList<>();
        ResultSet resultSet = null;
        try {
            resultSet = findUsersStmt.executeQuery();
            while (resultSet.next()) {
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
     *
     * @param username attempted login username
     * @param password attempted login password
     * @return UserBean if user is found in db, null if credentials not matching
     */
    public UserBean findUser(String username, String password) {
        ResultSet resultSet = null;
        UserBean user = null;
        int resultSize = 0;
        int id = 0;
        try {
            //set the params for the PreparedStatement
            findUserStmt.setString(1, username);
            findUserStmt.setString(2, password);

            resultSet = findUserStmt.executeQuery();

            if (resultSet.next()) {
                user = new UserBean(resultSet.getInt("id"), resultSet.getString("username"),
                        resultSet.getBoolean("admin"), resultSet.getInt("gamesPlayed")
                );
                System.out.println("user: " + user.getID() + user.getUsername() + "logged in. (DAO)");
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
     * Fetches ALL the words in a certain language from the database. For Swedish about 1.2k.
     *
     * @param languageID the language in which the user wants to play
     * @return list of WordBeans, one per word in the bd, with id word and clue
     */
    public ArrayList<WordBean> findAllWords(int languageID) {
        ArrayList<WordBean> words = new ArrayList<>();
        ResultSet resultSet = null;
        try {
            findWordsStmt.setInt(1, languageID);
            resultSet = findWordsStmt.executeQuery();
            while (resultSet.next()) {
                words.add(new WordBean(resultSet.getInt(1),
                        resultSet.getString(2), resultSet.getString(3)));
            }
            connection.commit();
        } catch (SQLException e) {
            exceptionHandler("Couldn't fetch words from the database for language " + languageID, e);
        } finally {
            closeResult(resultSet);
        }
        return words;
    }

    /**
     * Returns one word, with the offset given as parameter
     *
     * @param languageID the language which the word is in
     * @param offset     how many words to skip; simulated randomness
     * @return a WordBean with a random word and it's related info from the database
     */
    public WordBean findRandomWord(int languageID, int offset) {
        WordBean word = null;
        ResultSet resultSet = null;
        try {
            findRandomWordStmt.setInt(1, languageID);
            findRandomWordStmt.setInt(2, offset);
            resultSet = findRandomWordStmt.executeQuery();
            if (resultSet.next()) {
                System.out.println("Ord from DAO föööre wordbean: " + resultSet.getString("word"));
                word = new WordBean(resultSet.getInt("id"),
                        resultSet.getString("word"), resultSet.getString("clue"));
                System.out.println("Ord from DAO efter wordbean: " + word.getWord());
            }
            connection.commit();
        } catch (SQLException e) {
            exceptionHandler("Couldn't fetch a random word from the database for language " + languageID, e);
        } finally {
            closeResult(resultSet);
        }
        return word;
    }

    /**
     * A user goes to settings and changes the settings. If user already had settings associated with them,
     * overwrite/update these with the new ones. If they had none, create a new entry in the database for this user.
     *
     * @param userID      the user for whom to update stored settings
     * @param newSettings SettingsBean with the new settings as chosen by the user
     */
    public void updateSettings(int userID, SettingsBean newSettings) {
        System.out.println("user wants to update the settings. language setting requested: " +
                newSettings.getLanguageID() + " " + newSettings.getLanguageName());
        int affectedRows = 0;
        try {
            updateSettingsStmt.setInt(1, newSettings.getLanguageID());
            updateSettingsStmt.setInt(2, newSettings.getSecondsPerRound());
            updateSettingsStmt.setInt(3, newSettings.getRoundsPerGame());
            updateSettingsStmt.setInt(4, userID);
            affectedRows = updateSettingsStmt.executeUpdate();
            if (affectedRows == 0) {
                //the user didn't have any saved settings since before so nothing was updated.
                //instead create new row for this user.
                createSettingsStmt.setInt(1, userID);
                createSettingsStmt.setInt(2, newSettings.getLanguageID());
                createSettingsStmt.setInt(3, newSettings.getSecondsPerRound());
                createSettingsStmt.setInt(4, newSettings.getRoundsPerGame());
                int createdRows = createSettingsStmt.executeUpdate();
                if (createdRows == 1) {
                    connection.commit();
                } else {
                    connection.rollback();
                    throw new SQLException("Couldn't create user settings");
                }
            } else if (affectedRows == 1) {
                connection.commit();
            } else {
                connection.rollback();
                throw new SQLException("Couldn't update user settings");
            }
        } catch (SQLException e) {
            exceptionHandler("Couldn't execute update/create user settings query: ", e);
        }
    }


    /**
     * Creates a new user entry in the DB when a user wants to create an account.
     * Will fail if username is already in use.
     *
     * @param username the username as entered by the aspiring user
     * @param password the suggested password from the user
     * @return status code: 0 if everything went well, otherwise -1
     */
    public int createAccount(String username, String password) {
        int affectedRows = 0;
        try {
            createUserStmt.setString(1, username);
            createUserStmt.setString(2, password);
            affectedRows = createUserStmt.executeUpdate();
            if (affectedRows != 1) {
                exceptionHandler("Couldn't create new user, maybe username already exists ", new SQLException());
                connection.rollback();
                return -1;
            } else
                connection.commit();
        } catch (SQLException | NullPointerException e) {
            exceptionHandler("Couldn't execute create user query, " +
                    "maybe username already in use? ", e);
            return -1;
        }
        return 0;
    }

    private void connectToDB() throws ClassNotFoundException, SQLException {
        Class.forName("org.apache.derby.jdbc.ClientDriver");
        connection = DriverManager.getConnection("jdbc:derby://localhost:1527/maoDB",
                System.getenv("dbuser"), System.getenv("dbpassword"));
        connection.setAutoCommit(false);
        System.out.println("Connected to database.");
    }

    private void prepareStatements() throws SQLException {
        findLanguagesStmt = connection.prepareStatement(
                "SELECT * FROM languages "
        );
        findReportsStmt = connection.prepareStatement(
                "SELECT * FROM reports"
        );
        findSettingsStmt = connection.prepareStatement(
                "SELECT * FROM user_settings JOIN languages ON user_settings.language_id = languages.id WHERE user_id = ? "
        );
        findUsersStmt = connection.prepareStatement(
                "SELECT username, gamesPlayed FROM users ORDER BY gamesPlayed"
        );
        findUserStmt = connection.prepareStatement(
                "SELECT id, username, admin, gamesPlayed FROM users " +
                        "WHERE username = ? AND password = ?"
        );
        findNoOfWordsStmt = connection.prepareStatement(        //Could theoretically yield problems if someone
                "SELECT COUNT(*) FROM words WHERE language_id = ? "
        );
        findWordsStmt = connection.prepareStatement(
                "SELECT id, word, clue FROM words WHERE language_id = ? "
        );
        findRandomWordStmt = connection.prepareStatement(
                "SELECT * FROM words WHERE language_id = ? " +
                        "OFFSET ? ROWS " +
                        "FETCH NEXT ROW ONLY "
        );
        updateWordCorrectStmt = connection.prepareStatement(
                "UPDATE words SET correctlyGuessed = correctlyGuessed +1 " +
                        "WHERE id = ? "
        );
        updateWordSkippedStmt = connection.prepareStatement(
                "UPDATE words SET skipped = skipped +1 " +
                        "WHERE id = ?"
        );
        updateSettingsStmt = connection.prepareStatement(
                "UPDATE user_settings " +
                        "SET language_id = ?, secondsPerRound = ?, roundsPerGame = ? " +
                        "WHERE user_id = ? "
        );
        updateUserGamesStmt = connection.prepareStatement(
                "UPDATE users SET gamesPlayed = gamesPlayed + 1 " +
                        "WHERE id = ?"
        );
//        updateUserGamesStmt = connection.prepareStatement(
//                "UPDATE users SET gamesPlayed = gamesPlayed + 1 " +
//                        "WHERE id = ?"
//        );
//        createReportStmt = connection.prepareStatement(
//                "INSERT INTO reports (timeOfReport, reportingUser, reportedWord, reason) " +
//                        "VALUES (now(), ?, ?, ?)"
//        );
        createUserStmt = connection.prepareStatement(
                "INSERT INTO users (username, password, admin, gamesplayed) " +
                        "VALUES (?, ?, false, 0)"
        );
        createSettingsStmt = connection.prepareStatement(
                "INSERT INTO user_settings (user_id, language_id, secondsPerRound, roundsPerGame)" +
                        "VALUES (?, ?, ?, ?) "
        );
//        createWordStmt = connection.prepareStatement(
//                "INSERT INTO words (word, clue, correctlyGuessed, skipped, flags) " +
//                        "VALUES (?, ?, 0, 0, 0)"
//        );
//        deleteUserStmt = connection.prepareStatement(
//                "DELETE FROM users WHERE id = ?"
//        );
//        deleteWordStmt = connection.prepareStatement(
//                "DELETE FROM words WHERE id = ?"
//        );
    }

    private void closeResult(ResultSet result) {
        try {
            result.close();
        } catch (SQLException | NullPointerException e) {
            System.out.println("Could not close result");
//            throw new RuntimeException(e);
        }
    }

    private void exceptionHandler(String failMsg, Exception exception) {
        System.out.println(failMsg + exception.getMessage());
        try {
            connection.rollback();
            exception.printStackTrace();
        } catch (SQLException  | NullPointerException e) {
            System.out.println("Could not rollback: " + e.getMessage());
            e.printStackTrace();
//            throw new RuntimeException(e);
        }
    }

}
