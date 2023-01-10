package kth.se.id1212.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import kth.se.id1212.model.*;

import java.io.IOException;
import java.util.*;

import kth.se.id1212.model.Game;

import java.sql.DriverManager;


@WebServlet(name = "ControllerServlet", value = "/ControllerServlet")
public class ControllerServlet extends HttpServlet {
    //OtherWordsDAO db = new OtherWordsDAO();
    Map<HttpSession, Game> activeSessions = new HashMap<>();

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
        // TODO Invalidate old sessions after certain time, if planning to actually run the server for other than project
        HttpSession session = request.getSession(true);
        Game game = (Game) session.getAttribute("gameModel");
        if (game == null) {
            game = new Game();
            session.setAttribute("gameModel", game);
        }
        activeSessions.put(session, game);

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
            //System.out.println("jspFile from Controller: " + jspFile);
            if (jspFile == null) {
                System.out.println("Seems the jspFile is null");
            } else if (jspFile.endsWith("/gameView.jsp")) {
                doGameView(request, response, session);
            } else if (jspFile.endsWith("/pauseView.jsp")) {
                doPauseView(request, response, session);
            } else if (jspFile.endsWith("/setUpView.jsp")) {
                doSetUpView(request, response, session);
            } else if (jspFile.endsWith("/settingsView.jsp")) {
                doSettingsView(request, response, session);
            } else if (jspFile.endsWith("/resultsView.jsp")) {
                doResultsView(request, response, session);
            } else if (jspFile.endsWith("/testView.jsp")) {
                doTestView(request, response, session);
            } else {
                //System.out.println("That jspFile is not handled");
                doIndex(request, response, session);
            }

    }

    private void doGameView(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
        String action = request.getParameter("action");
        Game game = activeSessions.get(session);
        // TODO Probably check logic, I should have been in bed
        switch (action) {
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
//            case "finalGuessSkip":
//                // TODO Call finalSkip or something
//                response.sendRedirect("pauseView.jsp");
//                break;
//            case "finalGuessCorrect":
//                // TODO Call finalCorrect or something
//                response.sendRedirect("pauseView.jsp");
//                break;
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
        Game game = activeSessions.get(session);
        int score = game.getScore();
        String currentWord = game.getCurrentWord();
        System.out.println("Setting current word from doGameViewProgress: " + currentWord);

        session.setAttribute("score", score);
        session.setAttribute("currentWord", currentWord);
        int timeLeft = game.getTimeLeft();
        session.setAttribute("timeLeft", timeLeft);

        if (timeLeft <= 0) {
            response.sendRedirect("pauseView.jsp");
        } else
            response.sendRedirect("gameView.jsp");
    }

    private void doSetUpView(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
        Game game = activeSessions.get(session);
        SettingsBean settingsBean = new SettingsBean(); // TODO Probably get from DB

        String action = request.getParameter("action");
        switch (action) {
            case "add":
                String teamToAdd = request.getParameter("team");
                game.addTeam(teamToAdd);

                session.setAttribute("teamsPlaying", game.getTeamsPlaying());

                response.sendRedirect("setUpView.jsp");
                break;
            case "remove":
                String teamToRemove = request.getParameter("teamToRemove");
                game.removeTeam(teamToRemove);

                session.setAttribute("teamsPlaying", game.getTeamsPlaying());

                response.sendRedirect("setUpView.jsp");
                break;
            case "start":
                game.newGame();
                int score = game.getScore();
                String currentWord = game.getCurrentWord();
                session.setAttribute("score", score);
                session.setAttribute("currentWord", currentWord);

                int timeLeft = settingsBean.getSecondsPerRound();
                session.setAttribute("timeLeft", timeLeft);
                session.setAttribute("currentTeamBean", game.getCurrentTeam());

                game.nextRound(settingsBean);

                session.setAttribute("nextTeamBean", game.getNextTeam()); // TODO Probably a better solution to this

                response.sendRedirect("gameView.jsp");
                break;
            default:
                System.out.println("Action was " + action + ". That was unexpected");
                break;
        }

    }

    private void doSettingsView(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
//        SettingsBean settingsBean = new SettingsBean(); // TODO Get from DB if user logged in
//        int roundTimeFromDB = settingsBean.getRoundTime();
//        session.setAttribute("roundTime", roundTimeFromDB);
//        int roundTime = Integer.parseInt(request.getParameter("roundTimeSlider"));
        response.sendRedirect("settingsView.jsp");

        /*
        Användaren har precis laddat sidan... Inga requests har gjorts. So... Om användaren loggar in...
        DÅ sätter vi settings från DB.
        Då kommer "rätt" settings att visas när användaren trycker sig in på settings.
        Om användaren inte har loggat in... Då vill vi visa default settings... Men då görs det inget request innan användaren kan se settings...
        Hmm...
        Kanske gör att köra en onload request och sen en if (parameter.request) do settings

        Eh, det måste ju bara vara att... Skapa ny settingsBean, finns det en användare inloggad. Hämta från DB. Annars standard.
        Sen när det
         */
    }

    private void doPauseView(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
        // TODO Fix settingsBean, this can only lead to confusion
        SettingsBean settingsBean = new SettingsBean();
        Game game = activeSessions.get(session);

        // TODO Could probably get rid of nextTeam by moving the setAttribute around. For example, set it after redirect... Possibly.
        game.nextRound(settingsBean);
        session.setAttribute("timeLeft", game.getTimeLeft());
        session.setAttribute("currentTeamBean", game.getCurrentTeam());
        session.setAttribute("nextTeamBean", game.getNextTeam());
        session.setAttribute("score", game.getScore());
        if (game.isGameOver()) {
            response.sendRedirect("resultsView.jsp");
        } else {
            response.sendRedirect("gameView.jsp");
        }

    }

    private void doResultsView(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
        response.sendRedirect("index.jsp");
    }

    private void doIndex(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
        String action = request.getParameter("action");
        switch (action) {
            case "play":
                response.sendRedirect("setUpView.jsp");
                break;
            case "settings":
                response.sendRedirect("settingsView.jsp");
                break;
            case "test":
                response.sendRedirect("testView.jsp");
                break;
            default:
                System.out.println("Action was " + action + ". That was unexpected. Sending to Index");
                response.sendRedirect("index.jsp");
                break;
        }

    }

    private void doTestView(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
        // Separat från resten, do what you want

        response.sendRedirect("testView.jsp");

    }

}