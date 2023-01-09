package kth.se.id1212.model;

import java.util.ArrayList;

public class TeamBean {
    private int id;
    private String name;
    private int score;
    private ArrayList<String> correctGuesses = new ArrayList<>();

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

    public ArrayList<String> getCorrectGuesses() {
        return correctGuesses;
    }

    public void setCorrectGuesses(ArrayList<String> correctGuesses) {
        this.correctGuesses = correctGuesses;
    }
}
