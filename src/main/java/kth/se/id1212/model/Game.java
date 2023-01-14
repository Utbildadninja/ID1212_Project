package kth.se.id1212.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Game {
    ApiCalls apiCalls = new ApiCalls();
    WordBean wordBean = null;
    ArrayList<WordBean> wordBeanArrayList = null;
    int currentRound = -1;
    String currentWord = null;
    int currentWordCounter = -1;
    String[] wordList;
    int timeLeft = 25;
    List<TeamBean> teamsPlaying = new ArrayList<>();
    TeamBean currentTeam;
    int totalRounds = 2;
    boolean gameOver = false;
    TeamBean nextTeam;

    public void newGame() throws IOException {
        if (wordList == null) {
            fetchNewArray();
            this.currentWordCounter = 0;
        }
        this.gameOver = false;
        currentRound = 0;
        currentTeam = teamsPlaying.get(0);
        for (TeamBean team : teamsPlaying) {
            team.setScore(0);
        }
        setCurrentWord();
//        if (teamsPlaying.size() > 1)
//            nextTeam = teamsPlaying.get(1);
//        else
//            nextTeam = currentTeam;
    }

    public void fetchNewArray() throws IOException {
        // TODO Add logic to choose where to get the words from
        //wordList = apiCalls.getNewArrayPremium();
        wordList = apiCalls.getNewArrayFree();
    }

    public void setCurrentWord() throws IOException {
        if (this.currentWordCounter >= wordList.length) {
            fetchNewArray();
            this.currentWordCounter = 0;
        } else if (this.currentWordCounter >= wordList.length - 1) {
            System.out.println("One word remaining in list");
        }

        // TODO If APIs are laggy, this could be moved to set before getting a new array when there's one word remaining.
        this.currentWord = wordList[currentWordCounter];
    }

    public void correct() throws IOException {
        currentWordCounter++;
        int score = currentTeam.getScore();
        currentTeam.setScore(score + 10);
        addCorrectGuess(currentWord);
        setCurrentWord();
    }

    public void skip() throws IOException {
        currentWordCounter++;
        setCurrentWord();
    }

    public void nextRound(SettingsBean settingsBean) {
        if (this.currentRound == this.totalRounds * teamsPlaying.size()) {
            gameOver();
        } else {
            currentTeam = teamsPlaying.get(currentRound % teamsPlaying.size());
            currentRound++;
            nextTeam = teamsPlaying.get(currentRound % teamsPlaying.size());
            System.out.println("Current round is: " + currentRound + " current Team is: " + currentTeam.getName());
            setTimeLeft(settingsBean.getSecondsPerRound());
            startTimer();
        }

    }

    public void addCorrectGuess(String word) {
        ArrayList<String> correctGuesses = currentTeam.getCorrectGuesses();
        correctGuesses.add(word);
        currentTeam.setCorrectGuesses(correctGuesses);
    }

    public void gameOver() {
        System.out.println("Game is finished");
        this.gameOver = true;
    }

    public void startTimer() {
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
        executor.scheduleAtFixedRate(new Runnable() {

            @Override
            public void run() {
                if (timeLeft > 0) {
                    //System.out.println("Time remaining: " + timeLeft + " seconds");
                    timeLeft--;
                } else {
//                    System.out.println("Time's up!");
                    executor.shutdown();
                    // send update to controller
                }
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    public void addTeam(String teamToAdd) {
        TeamBean teamBean = new TeamBean();
        teamBean.setName(teamToAdd);
        teamsPlaying.add(teamBean);
//        System.out.println("Added team: " + teamBean.getName() + " with ID " + teamBean.getId() + " ID will always be 0 for now");
//        System.out.println("Teams in list:");
//        for (TeamBean bean : teamsPlaying) {
//            System.out.println("Team ID: " + bean.getId());
//            System.out.println("Team Name: " + bean.getName());
//        }
    }

    public void removeTeam(String name) {
        teamsPlaying.removeIf(team -> team.getName().equals(name));
    }

    public List<TeamBean> getTeamsPlaying() {
        return teamsPlaying;
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
        return currentTeam.getScore();
    }

    public int getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(int timeLeft) {
        this.timeLeft = timeLeft;
    }

    public TeamBean getCurrentTeam() {
        return currentTeam;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public TeamBean getNextTeam() {
        return nextTeam;
    }

    public void setTotalRounds(int totalRounds) {
        this.totalRounds = totalRounds;
    }
}
