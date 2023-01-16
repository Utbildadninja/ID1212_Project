package kth.se.id1212.model;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
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

    /**
     * Calls the RESTful API to get 20 words.
     * Minimum 5 characters.
     * API KEY has to be used to access this API.
     *
     * @return A String array
     * @throws IOException Anything that goes wrong
     */
    public String[] getNewArrayPremium() throws IOException {

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
        List<WordBean> wordBeans = new ArrayList<>();
        Gson gson = new Gson();
        JsonReader reader = new JsonReader(new StringReader(apiResponseBody));
        reader.beginArray();
        while (reader.hasNext()) {
            WordBean wordBean = gson.fromJson(reader, WordBean.class);
            wordBeans.add(wordBean);
        }
        reader.endArray();

        // Iterate over the list and print the word for each object
        String[] wordListStringArray = new String[wordBeans.size()];
        int counter = 0;
        for (WordBean wordBean : wordBeans) {
            wordListStringArray[counter++] = wordBean.getWord();
        }
        return wordListStringArray;
    }

    public String[] getNewArrayFree() {
        HttpResponse<String> apiResponse = null;
        try {
            apiResponse = client.send(freeApiRequest, HttpResponse.BodyHandlers.ofString());
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
        String apiResponseBody = apiResponse.body();

        System.out.println("Sent to API: " + freeApiRequest);
        System.out.println("API Response: " + apiResponseBody);

        String[] wordListStringArray = new Gson().fromJson(apiResponseBody, String[].class);

        return wordListStringArray;
    }

    private String getAPI_KEY() {
        return System.getenv("api_key");
    }

}


