package kth.se.id1212.model;

import kth.se.id1212.integration.OtherWordsDAO;

import javax.sound.midi.Soundbank;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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
    SettingsBean settingsBean;
    OtherWordsDAO db;
//    OtherWordsDAO db = new OtherWordsDAO(); // TODO db sent from controller, keep in mind might cause problems

    public Game(){

    }

    public Game(OtherWordsDAO dbConnection){
        this.db = dbConnection;
    }

    public void newGame() throws IOException {
        if (wordList == null) {
            fetchNewArray();
            this.currentWordCounter = 0;
        }
        this.gameOver = false;
        currentRound = 0;
        currentTeam = teamsPlaying.get(0);
        nextTeam = teamsPlaying.get(0);
        for (TeamBean team : teamsPlaying) {
            team.setScore(0);
            team.setCorrectGuesses(new ArrayList<>());
        }
        setCurrentWord();
        setTotalRounds(settingsBean.getRoundsPerGame());
        setTimeLeft(settingsBean.getSecondsPerRound());
    }

    /**
     * When we start a new game, we get words from either API or database.
     * @throws IOException
     */
    public void fetchNewArray() throws IOException {
        LanguageBean languageBean = settingsBean.getLanguageBean();
        String languageName = settingsBean.getLanguageName();
        int languageID = settingsBean.getLanguageID();
        System.out.println("language set to " + languageBean.getLanguageName());
        if(languageName.equals("Test_API")){
            wordList = apiCalls.getNewArrayFree();
            //for (String word : wordList){}        TODO convert to WordBean if reports are to be implemented
        }
        else if(languageName.equals("English_API")){
            wordList = apiCalls.getNewArrayPremium();
        }
        else if(languageID!=0){
            this.wordBeanArrayList = fetchWordBeansFromDB(languageID);
            String[] onlyWords = new String[wordBeanArrayList.size()];
            for (int i = 0; i < wordBeanArrayList.size(); i++) {        //Converts Beans to Strings
                onlyWords[i] = wordBeanArrayList.get(i).getWord();
                System.out.println(onlyWords[i]);
            }
            this.wordList = onlyWords;
        }
        else {
            System.out.println("The language/word source setting is not behaving as expected. ");
        }
    }

    //helper method to get random words (WordBeans) from the bd (sadly one at a time)
    private ArrayList<WordBean> fetchWordBeansFromDB(int languageID ){
        int noOfWordsToFetch = 10;
        int totalNoOfWordsInDB = db.findNoOfWords(languageID);
        System.out.println(
                "the total number of words in the db of language " + languageID + " was " + totalNoOfWordsInDB);
        ArrayList<WordBean> wordBeans = new ArrayList<>();
        Random rand = new Random();
        int[] offsets = new int[noOfWordsToFetch];
        for (int i =0; i < noOfWordsToFetch ; i++){
            offsets[i] = rand.nextInt(totalNoOfWordsInDB);
        }
        for (int i =0; i < noOfWordsToFetch ; i++){
            wordBeans.add(db.findRandomWord(languageID, offsets[i]));
        }
        return wordBeans;
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
        executor.scheduleAtFixedRate(() -> {
            if (timeLeft > 0) {
                //System.out.println("Time remaining: " + timeLeft + " seconds");
                timeLeft--;
            } else {
//                    System.out.println("Time's up!");
                executor.shutdown();
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    public void addTeam(String teamToAdd) {
        boolean exists = false;
        for (TeamBean team:teamsPlaying) {
            if (team.getName().equals(teamToAdd)) {
                exists = true;
                break;
            }
        }
        if (!exists) {
            TeamBean teamBean = new TeamBean();
            teamBean.setName(teamToAdd);
            teamsPlaying.add(teamBean);
        } else System.out.println("Tried to add duplicate teams");

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

    public void setSettingsBean(SettingsBean settingsBean) {
        this.settingsBean = settingsBean;
    }

    public SettingsBean getSettingsBean() {
        return settingsBean;
    }
}
