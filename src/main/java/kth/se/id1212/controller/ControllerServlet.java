package kth.se.id1212.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import kth.se.id1212.model.*;

import java.io.IOException;
import java.util.ArrayList;

import kth.se.id1212.model.Game;

import java.sql.DriverManager;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


@WebServlet(name = "ControllerServlet", value = "/ControllerServlet")
public class ControllerServlet extends HttpServlet {
    //OtherWordsDAO db = new OtherWordsDAO();
    Game game = new Game();


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Received GET request");
        handleRequest(request, response);
    }

    // Handles all POST requests
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("Received POST request");
        System.out.println(DriverManager.getDrivers().getClass().getName()); //TODO remove
        handleRequest(request, response);
    }

    private void handleRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession(true);

        // Prints all parameters for the request, simply for debugging
        System.out.println("Printing parameters in this request");
        Map<String, String[]> parameterMap = request.getParameterMap();
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            String parameterName = entry.getKey();
            String[] parameterValues = entry.getValue();
            System.out.println(parameterName + ": " + Arrays.toString(parameterValues));
        }
        System.out.println("Parameters printed, below this is other stuff");

        // Checks the parameter "jspFile" to point the request to correct method
        String jspFile = request.getParameter("jspFile");
        System.out.println("jspFile from Controller: " + jspFile);
        if (jspFile == null) {
            System.out.println("Seems the jspFile is null");
        }
        else if (jspFile.endsWith("/gameView.jsp")) {
            doGameView(request, response, session);
        }
        else if (jspFile.endsWith("/pauseView.jsp")) {
            doPauseView(request, response, session);
        }
        else if (jspFile.endsWith("/setUpView.jsp")) {
            doSetUpView(request, response, session);
        }
        else if (jspFile.endsWith("/settingsView.jsp")) {
            doSettingsView(request, response, session);
        }
        else if (jspFile.endsWith("/resultsView.jsp")) {
            doResultsView(request, response, session);
        }
        else if (jspFile.endsWith("/testView.jsp")) {
            doTestView(request, response, session);
        }
    }

    private void doGameView(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
        String action = request.getParameter("action");
        // TODO Probably check logic, I should have been in bed
        switch (action) {
            case "getTimeRemaining":
                int timeRemaining = game.getTimeLeft();
                response.getWriter().println(timeRemaining);
            case "correct":
                System.out.println("Action was: " + action);
                game.correct();
                doGameViewProgress(request, response, session);
                break;
            case "skip":
                System.out.println("Action was: " + action);
                game.skip();
                doGameViewProgress(request, response, session);
                break;
            case "finalGuessSkip":
                // TODO Call finalSkip or something
                response.sendRedirect("pauseView.jsp");
                break;
            case "finalGuessCorrect":
                // TODO Call finalCorrect or something
                response.sendRedirect("pauseView.jsp");
                break;
            case "results":
                // TODO Call results or something
                response.sendRedirect("resultsView.jsp");
                break;
            default:
                System.out.println("Action was " + action + ". That was unexpected");
                break;
        }

    }

    private void doGameViewProgress(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
        int score = game.getScore();
        String currentWord = game.getCurrentWord();
        System.out.println("Setting current word from doGameViewProgress: " + currentWord);

        session.setAttribute("score", score);
        session.setAttribute("currentWord", currentWord);
        int timeLeft = game.getTimeLeft();
        session.setAttribute("timeLeft", timeLeft);

        if (timeLeft <= 0) {
            game.nextRound();
            response.sendRedirect("pauseView.jsp");
        }

        else
            response.sendRedirect("gameView.jsp");
    }

    private void doSetUpView(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
        SettingsBean settingsBean = new SettingsBean();


        String action = request.getParameter("action");
        switch (action) {
            case "add":
                String teamToAdd = request.getParameter("team");
                game.addTeam(teamToAdd);

                session.setAttribute("teamsPlaying", game.getTeamsPlaying());

                response.sendRedirect("setUpView.jsp");
                break;
            case "start":
                game.newGame();
                int score = game.getScore();
                String currentWord = game.getCurrentWord();
                session.setAttribute("score", score);
                session.setAttribute("currentWord", currentWord);

                int timeLeft = settingsBean.getRoundTime();
                game.setTimeLeft(timeLeft);
                session.setAttribute("timeLeft", timeLeft);

                game.nextRound();

                response.sendRedirect("gameView.jsp");
                break;
            default:
                System.out.println("Action was " + action + ". That was unexpected");
                break;
        }

    }

    private void doSettingsView(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {

        response.sendRedirect("settingsView.jsp");
    }

    private void doPauseView(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
        // TODO Prepare for next team
        response.sendRedirect("gameView.jsp");
    }

    private void doResultsView(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
        response.sendRedirect("index.jsp");
    }

    private void doTestView(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
        // Separat från resten, do what you want
        
        response.sendRedirect("testView.jsp");

    }

}