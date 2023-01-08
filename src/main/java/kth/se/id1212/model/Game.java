package kth.se.id1212.model;

import kth.se.id1212.controller.ControllerServlet;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Game {
    // TODO Add a Team object, containing name, score etc
    ApiCalls apiCalls = new ApiCalls();
    WordBean wordBean = null;
    int currentRound = -1;
    String currentWord = null;
    int currentWordCounter = -1;
    String[] wordList;
    int score = 0;
    int timeLeft = 15; // TODO Set with settings

    public void newGame() throws IOException {
        if (wordList == null) {
            getNewArray();
            this.currentWordCounter = 0;
        }

        this.score = 0;
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
        score += 10;
        setCurrentWord();
    }

    public void skip() throws IOException {
        currentWordCounter++;
        setCurrentWord();
    }

    public void next() {
        startTimer();
    }

    public void startTimer() {
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
        executor.scheduleAtFixedRate(new Runnable() {

            @Override
            public void run() {
                if (timeLeft > 0) {
                    System.out.println("Time remaining: " + timeLeft + " seconds");
                    timeLeft--;
                } else {
                    System.out.println("Time's up!");
                    executor.shutdown();
                    // send update to controller
                }
            }
        }, 0, 1, TimeUnit.SECONDS);
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

    public int getScore() {
        return score;
    }

    public int getTimeLeft() {
        return timeLeft;
    }
}
