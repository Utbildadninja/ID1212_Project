package kth.se.id1212.model;

public class SettingsBean {
    private int roundTime = 15;
    private int languageID;
    private String languageName;
    private int roundsPerGame;

    public int getRoundTime() {
        return roundTime;
    }

    public void setRoundTime(int roundTime) {
        this.roundTime = roundTime;
    }
}
