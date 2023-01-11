package kth.se.id1212.model;

public class SettingsBean {
    private int secondsPerRound = 10;
    private int languageID;
    private String languageName; //TODO change sql to also fetch language name (join)
    private int roundsPerGame = 2;
    private String wordSource = "free";

    public SettingsBean() {}

    public SettingsBean(int languageID, int secondsPerRound, int roundsPerGame){
        this.languageID = languageID;
        this.secondsPerRound = secondsPerRound;
        this.roundsPerGame = roundsPerGame;
    }

    public int getSecondsPerRound() {
        return secondsPerRound;
    }

    public void setSecondsPerRound(int secondsPerRound) {
        this.secondsPerRound = secondsPerRound;
    }

    public int getRoundsPerGame() {
        return roundsPerGame;
    }

    public void setRoundsPerGame(int roundsPerGame) {
        this.roundsPerGame = roundsPerGame;
    }

    public String getWordSource() {
        return wordSource;
    }

    public void setWordSource(String wordSource) {
        this.wordSource = wordSource;
    }

    public int getLanguageID() {
        return languageID;
    }

    public void setLanguageID(int languageID) {
        this.languageID = languageID;
    }

    public String getLanguageName() {
        return languageName;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }
}
