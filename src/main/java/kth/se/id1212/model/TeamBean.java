package kth.se.id1212.model;

public class TeamBean {
    private int id;
    private String name;
    private int score;
    private String[] correctGuesses;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String[] getCorrectGuesses() {
        return correctGuesses;
    }

    public void setCorrectGuesses(String[] correctGuesses) {
        this.correctGuesses = correctGuesses;
    }
}
