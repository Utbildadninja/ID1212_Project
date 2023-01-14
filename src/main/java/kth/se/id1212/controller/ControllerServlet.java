package kth.se.id1212.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import kth.se.id1212.integration.OtherWordsDAO;
import kth.se.id1212.model.*;

import java.io.IOException;
import java.util.*;

import kth.se.id1212.model.Game;

import java.sql.DriverManager;

@WebServlet(name = "ControllerServlet", value = "/ControllerServlet")
public class ControllerServlet extends HttpServlet {
    OtherWordsDAO db = new OtherWordsDAO();
    // WeakHashMap allows the Java Garbage collector to remove invalidated sessions
    // containsKey() can be used to check if the key is still in place, to avoid any NullPointerException errors
    Map<HttpSession, Game> activeSessions = new WeakHashMap<>(); // TODO If any issues with HashMap arrive, change back to regular from Weak

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
        } else if (jspFile.endsWith("/loginView.jsp")) {
            doLoginView(request, response, session);
        } else if (jspFile.endsWith("/createAccountView.jsp")) {
            doCreateAccountView(request, response, session);
        } else if (jspFile.endsWith("/testView.jsp")) {
            doTestView(request, response, session);
        } else {
            doIndex(request, response, session);
        }

    }

    private void doCreateAccountView(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirm_password = request.getParameter("confirm_password");

        if (username != null && password != null && password.equals(confirm_password)) {
            // TODO Create account in Database
            db.createAccount(username, password);
            System.out.println("Account created");
            UserBean user;
            user = db.findUser(username, password);
            if (user != null) {
                session.setAttribute("userBean", user);
                System.out.println("User: " + user.getUsername() + " logged in");
                response.sendRedirect("index.jsp");
            } else {
                System.out.println("Login failed");
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid username or password");
            }
        } else {
            // TODO Add HTML5 form validation to client side or something
            System.out.println("Password mismatch");
            response.sendRedirect("createAccountView.jsp");
        }

    }

    private void doLoginView(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
        String action = request.getParameter("action");

        switch (action) {
            case "login":
                String username = request.getParameter("username");
                String password = request.getParameter("password");

                if (username != null && password != null) {
                    UserBean user;
                    user = db.findUser(username, password);

                    if (user != null) {
                        session.setAttribute("userBean", user);
                        System.out.println("User: " + user.getUsername() + " logged in");
                        response.sendRedirect("index.jsp");

                    } else {
                        System.out.println("Login failed");
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid username or password");
                    }
                } else {
                    System.out.println("Username or password was null");
                }
                break;
            case "createAccount":
                response.sendRedirect("createAccountView.jsp");
                break;
            default:
                System.out.println("Action was " + action + ". That was unexpected. Sending to login again.");
                response.sendRedirect("loginView.jsp");
                break;
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
        UserBean userBean = (UserBean) session.getAttribute("userBean");
        SettingsBean settingsBean;
        // Makes sure there's always a settingsBean, even if user never bothered to check the settings
        if (userBean != null) {
            System.out.println("Getting settingsBean from DB, in case user never entered settings");
            settingsBean = db.findUserSettings(userBean.getID());
            if (settingsBean != null)
                session.setAttribute("settingsBean", settingsBean);
            else
                System.out.println("A logged in user tried to start a new game with no stored settings");

        } else {
            System.out.println("Getting settingsBean from session for logged out user");
            settingsBean = (SettingsBean) session.getAttribute("settingsBean");
        }
        if (settingsBean == null) {
            settingsBean = new SettingsBean();
            session.setAttribute("settingsBean", settingsBean);
            System.out.println("Created a new settingsBean and set it to that, god knows why");
        }

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
                int timeLeft = settingsBean.getSecondsPerRound();
                int roundsPerGame = settingsBean.getRoundsPerGame();

                System.out.println("Time left when starting: " + timeLeft);
                session.setAttribute("score", score);
                session.setAttribute("currentWord", currentWord);
                session.setAttribute("currentTeamBean", game.getCurrentTeam());
                session.setAttribute("timeLeft", timeLeft);

                game.setTimeLeft(timeLeft);
                game.setTotalRounds(roundsPerGame);

                game.nextRound(settingsBean);

                session.setAttribute("nextTeamBean", game.getNextTeam()); // TODO Probably a better solution to this

                System.out.println("Time left when really starting: " + timeLeft);
                response.sendRedirect("gameView.jsp");
                break;
            default:
                System.out.println("Action was " + action + ". That was unexpected");
                break;
        }

    }

    private void doSettingsView(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
        int roundTimeSlider = Integer.parseInt(request.getParameter("roundTimeSlider"));
        int numberOfRounds = Integer.parseInt(request.getParameter("numberOfRounds"));
        String language = request.getParameter("language");

        UserBean userBean = (UserBean) session.getAttribute("userBean");
        // Always set when hitting Settings, so should never be null.
        SettingsBean settingsBean = (SettingsBean) session.getAttribute("settingsBean");
        if (userBean != null ) {
            System.out.println("A logged in user tried to update settings");
            settingsBean.setSecondsPerRound(roundTimeSlider);
            settingsBean.setRoundsPerGame(numberOfRounds);
            settingsBean.setLanguageName(language);
            session.setAttribute("settingsBean", settingsBean);
            db.updateSettings(userBean.getID(), settingsBean);            // TODO Set settings to DB.
        } else {
            System.out.println("A logged out user tried to update settings");
            settingsBean.setSecondsPerRound(roundTimeSlider);
            settingsBean.setRoundsPerGame(numberOfRounds);
            settingsBean.setLanguageName(language);
            session.setAttribute("settingsBean", settingsBean);
        }

        response.sendRedirect("settingsView.jsp");
    }

    private void doPauseView(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
        // SettingsBean is always checked for != null when starting a new game
        SettingsBean settingsBean = (SettingsBean) session.getAttribute("settingsBean");

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
                UserBean userBean = (UserBean) session.getAttribute("userBean");
                SettingsBean settingsBean;
                if (userBean != null ) {
                    System.out.println("A logged in user entered Settings");
                    settingsBean = db.findUserSettings(userBean.getID());
                    if(settingsBean != null) {
                        System.out.println("That user has previous saved settings");
                        session.setAttribute("settingsBean", settingsBean);
                    } else {
                        System.out.println("That user has no stored settings");
                        settingsBean = new SettingsBean();
                        session.setAttribute("settingsBean", settingsBean);
                    }
                } else {
                    settingsBean = (SettingsBean) session.getAttribute("settingsBean");
                    if (settingsBean == null)
                        settingsBean = new SettingsBean();
                    System.out.println("This user was not logged in");
                    session.setAttribute("settingsBean", settingsBean);
                }

                ArrayList<LanguageBean> languages = db.findLanguages();
                session.setAttribute("languages", languages);
                response.sendRedirect("settingsView.jsp");
                break;
            case "test":
                response.sendRedirect("testView.jsp");
                break;
            case "login":
                response.sendRedirect("loginView.jsp");
                break;
            case "logout":
                session.invalidate();
                response.sendRedirect("index.jsp");
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