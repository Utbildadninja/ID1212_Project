package kth.se.id1212.model;

import kth.se.id1212.controller.ControllerServlet;

import java.io.IOException;
import java.util.List;

public class Game {
    ApiCalls apiCalls = new ApiCalls();
    WordBean wordBean = null;
    int currentRound = -1;
    String currentWord = null;
    int currentWordCounter = -1;
    String[] wordList;

    public void newGame() throws IOException {
        if (wordList == null) {
            getNewArray();
            this.currentWordCounter = 0;
        }

        currentRound = 0;
    }

    public void getNewArray() throws IOException {
        // TODO Add logic to choose where to get the words from
        wordList = callPremiumAPI();
    }

    public void setCurrentWord () throws IOException {
        if (this.currentWordCounter >= wordList.length) {
            this.currentWordCounter = 0;
        }

        //System.out.println("API response from Game: " + wordBeans)

        this.currentWord = wordList[currentWordCounter];

    }

    public String getCurrentWord() {
        return currentWord;
    }

    public String[] callPremiumAPI() throws IOException {

        return apiCalls.getNewArrayPremium();
    }
}
