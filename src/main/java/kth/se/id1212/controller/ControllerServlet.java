package kth.se.id1212.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

@WebServlet(name = "ControllerServlet", value = "/ControllerServlet")
public class ControllerServlet extends HttpServlet {
    final String API_URL_FREE = "https://random-word-api.herokuapp.com/";
    final String API_URL = "https://api.wordnik.com/v4/";
    final String API_KEY = getAPI_KEY();

    String FreeEnd = "word?number=10";
    String PremiumEnd = "words.json/randomWords?hasDictionaryDef=true&maxCorpusCount=-1&minDictionaryCount=19&maxDictionaryCount=-1&minLength=5&maxLength=-1&limit=20&api_key=";
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest apiRequest = HttpRequest.newBuilder()
                .uri(URI.create(API_URL + PremiumEnd + API_KEY))
                .build();
        HttpResponse<String> apiResponse = null;
        try {
            apiResponse = client.send(apiRequest, HttpResponse.BodyHandlers.ofString());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        String apiResponseBody = apiResponse.body();
        // Process the API response
        System.out.println("Sent to API: " + apiRequest);
        System.out.println("API Response: " + apiResponseBody);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

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
}