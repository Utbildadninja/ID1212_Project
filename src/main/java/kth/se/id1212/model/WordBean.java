package kth.se.id1212.model;

public class WordBean {
    private int id;
    private String word;
    private String clue;

    public WordBean(int id, String word, String clue){
        this.id = id;
        this.word = word;
        this.clue = clue;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }
}
