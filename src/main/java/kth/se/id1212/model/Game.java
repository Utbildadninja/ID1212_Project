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
        setCurrentWord();
    }

    public void getNewArray() throws IOException {
        // TODO Add logic to choose where to get the words from
        //wordList = apiCalls.getNewArrayPremium();
        wordList = apiCalls.getNewArrayFree();
    }

    public void setCurrentWord() throws IOException {
        if (this.currentWordCounter >= wordList.length) {
            getNewArray();
            this.currentWordCounter = 0;
        } else if (this.currentWordCounter >= wordList.length -1) {
            System.out.println("One word remaining in list");
        }

        // TODO If APIs are laggy, this could be moved to set before getting a new array when there's one word remaining.
        this.currentWord = wordList[currentWordCounter];
    }

    public void correct () throws IOException {
        currentWordCounter++;
        setCurrentWord();
    }

    public void skip() throws IOException {
        currentWordCounter++;
        setCurrentWord();
    }

    public String getCurrentWord() {
        return currentWord;
    }

    public int getCurrentWordCounter() {
        return currentWordCounter;
    }

    public void setCurrentWordCounter(int currentWordCounter) {
        this.currentWordCounter = currentWordCounter;
    }



}
