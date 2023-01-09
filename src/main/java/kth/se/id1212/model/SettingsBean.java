package kth.se.id1212.model;

public class SettingsBean {
    private int roundTime = 15;
    private int languageID;
    private String languageName;
    private int roundsPerGame = 2;
    private String wordSource = "free";

    public int getRoundTime() {
        return roundTime;
    }

    public void setRoundTime(int roundTime) {
        this.roundTime = roundTime;
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
}
