package kth.se.id1212.model;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ApiCalls {
    private final HttpClient client = HttpClient.newHttpClient();
    private final String API_URL_FREE = "https://random-word-api.herokuapp.com/";
    private final String API_URL = "https://api.wordnik.com/v4/";
    private final String API_KEY = getAPI_KEY();

    private final String FreeEnd = "word?number=10";
    private final String PremiumEnd = "words.json/randomWords?hasDictionaryDef=true&maxCorpusCount=-1&minDictionaryCount=19&maxDictionaryCount=-1&minLength=5&maxLength=-1&limit=20&api_key=";

    // Immutable, needs one HttpRequest for each different call unless they are identical
    private final HttpRequest premiumApiRequest = HttpRequest.newBuilder()
            .uri(URI.create(API_URL + PremiumEnd + API_KEY))
            .build();

    private final HttpRequest freeApiRequest = HttpRequest.newBuilder()
            .uri(URI.create(API_URL_FREE + FreeEnd))
            .build();

    public String getNewArrayPremium() throws IOException {

        HttpResponse<String> apiResponse = null;
        try {
            apiResponse = client.send(premiumApiRequest, HttpResponse.BodyHandlers.ofString());
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
        String apiResponseBody = apiResponse.body();

        System.out.println("Sent to API: " + premiumApiRequest);
        System.out.println("API Response: " + apiResponseBody);

        // Parse the API response into a list of objects
        List<Word> words = new ArrayList<>();
        Gson gson = new Gson();
        JsonReader reader = new JsonReader(new StringReader(apiResponseBody));
        reader.beginArray();
        while (reader.hasNext()) {
            Word word = gson.fromJson(reader, Word.class);
            words.add(word);
        }
        reader.endArray();

        // Iterate over the list and print the word for each object
        for (Word word : words) {
            System.out.println(word.getWord());
        }

        System.out.println("Some kind of array: " + words);

        return apiResponseBody;
    }

    public String getNewArrayFree() {
        HttpResponse<String> apiResponse = null;
        try {
            apiResponse = client.send(freeApiRequest, HttpResponse.BodyHandlers.ofString());
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
        String apiResponseBody = apiResponse.body();

        System.out.println("Sent to API: " + premiumApiRequest);
        System.out.println("API Response: " + apiResponseBody);

        return apiResponseBody;
    }

    private String getAPI_KEY() {
        String API_KEY = null;

        try {
            Scanner scanner = new Scanner(new File("C:\\API_KEY.txt"));
            API_KEY = scanner.nextLine();
            scanner.close();
        } catch (FileNotFoundException e) {
            System.err.println("Scanner failed in the most horrible way");
            e.printStackTrace();
        }

        return API_KEY;
    }

    public static class Word {
        private int id;
        private String word;

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
}


