package kth.se.id1212.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import kth.se.id1212.integration.OtherWordsDAO;
import kth.se.id1212.model.*;

import java.io.IOException;
import java.util.*;

@WebServlet(name = "ControllerServlet", value = "/ControllerServlet")
public class ControllerServlet extends HttpServlet {
    OtherWordsDAO db = new OtherWordsDAO();
    // WeakHashMap allows the Java Garbage collector to remove invalidated sessions
    Map<HttpSession, Game> activeSessions = new WeakHashMap<>();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handleRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        handleRequest(request, response);
    }

    private void handleRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession(true);
        session.removeAttribute("errorMessage");

        if (!activeSessions.containsKey(session)) {
            Game game = new Game(db);
            activeSessions.put(session, game);
        }

        /*        // Prints all parameters for the request, simply for debugging
        System.out.println("Printing parameters in this request");
        Map<String, String[]> parameterMap = request.getParameterMap();
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            String parameterName = entry.getKey();
            String[] parameterValues = entry.getValue();
            System.out.println(parameterName + ": " + Arrays.toString(parameterValues));
        }
        System.out.println("Parameters printed, below this is other stuff");*/


        // Checks the parameter "jspFile" to point the request to correct method
        String jspFile = request.getParameter("jspFile");
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
        } else if (jspFile.endsWith("/instructionsView.jsp")) {
            doInstructionsView(request, response, session);
        } else {
            doIndex(request, response, session);
        }
    }

    private void doCreateAccountView(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirm_password = request.getParameter("confirm_password");

        if (username != null && password != null && password.equals(confirm_password)) {
            int createStatus = db.createAccount(username, password);
            System.out.println("Account created");

            // Login with new account and send to index
            if (createStatus == 0) {
                login(response, session, username, password);
            } else {
                System.out.println("Create account failed, status not 0");
                session.setAttribute("errorMessage", "Database error, account already exists");
                response.sendRedirect("createAccountView.jsp");
            }

        } else {
            System.out.println("Password mismatch");
            session.setAttribute("errorMessage", "Passwords do not match.");
            response.sendRedirect("createAccountView.jsp");
        }
    }

    private void login(HttpServletResponse response, HttpSession session, String username, String password) throws IOException {
        UserBean user;
        user = db.findUser(username, password);
        if (user != null) {
            session.setAttribute("userBean", user);
            System.out.println("User: " + user.getUsername() + " logged in");
            response.sendRedirect("index.jsp");
        } else {
            System.out.println("Login failed");
            session.setAttribute("errorMessage", "Incorrect username or password.");
            response.sendRedirect("loginView.jsp");
        }
    }

    private void doLoginView(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
        String action = request.getParameter("action");

        switch (action) {
            case "login":
                String username = request.getParameter("username");
                String password = request.getParameter("password");

                if (username != null && password != null) {
                    login(response, session, username, password);
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
        switch (action) {
            case "correct":
                game.correct();
                doGameViewProgress(request, response, session);
                break;
            case "skip":
                game.skip();
                doGameViewProgress(request, response, session);
                break;
            case "results":
                response.sendRedirect("resultsView.jsp");
                break;
            case "timer_up":
                session.setAttribute("timeLeft", game.getTimeLeft());
                response.sendRedirect("gameView.jsp");
                break;
            default:
                System.out.println("Action was " + action + ". That was unexpected");
                break;
        }
    }

    private void doGameViewProgress(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
        Game game = activeSessions.get(session);
        session.setAttribute("score", game.getScore());
        session.setAttribute("currentWord", game.getCurrentWord());
        int timeLeft = game.getTimeLeft();
        session.setAttribute("timeLeft", timeLeft);

        if (game.roundOver()) {
            session.setAttribute("gameOver", game.isLastRoundPlayed());
            response.sendRedirect("pauseView.jsp");
        } else
            response.sendRedirect("gameView.jsp");
    }

    private void doSetUpView(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
        Game game = activeSessions.get(session);

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
                SettingsBean settingsBean = preventNullSettingsBean(session);
                game.setSettingsBean(settingsBean);
                game.newGame();
                session.setAttribute("gameOver", game.isGameOver());
                session.setAttribute("nextTeamBean", game.getNextTeam());

                response.sendRedirect("pauseView.jsp");
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
        ArrayList<LanguageBean> languageBeans = (ArrayList<LanguageBean>) session.getAttribute("languages");
        UserBean userBean = (UserBean) session.getAttribute("userBean");
        // Always set when hitting Settings, so should never be null.
        SettingsBean settingsBean = (SettingsBean) session.getAttribute("settingsBean");
        applySettingsToSession(session, roundTimeSlider, numberOfRounds, language, languageBeans, settingsBean);
        if (userBean != null) {
            db.updateSettings(userBean.getID(), settingsBean);
        }

        response.sendRedirect("settingsView.jsp");
    }

    private void applySettingsToSession(HttpSession session, int roundTimeSlider, int numberOfRounds, String language, ArrayList<LanguageBean> languageBeans, SettingsBean settingsBean) {
        settingsBean.setSecondsPerRound(roundTimeSlider);
        settingsBean.setRoundsPerGame(numberOfRounds);
        if (language != null) {
            settingsBean.setLanguageName(language);
            for (LanguageBean languageBean : languageBeans) {
                if (languageBean.getLanguageName().equals(language)) {
                    settingsBean.setLanguageID(languageBean.getLanguageID());
                    settingsBean.setLanguageBean(languageBean);
                }
            }
        }
        session.setAttribute("settingsBean", settingsBean);
    }

    private void doPauseView(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
        // SettingsBean always exists at this stage
        SettingsBean settingsBean = (SettingsBean) session.getAttribute("settingsBean");
        Game game = activeSessions.get(session);

        game.nextRound(settingsBean);
        session.setAttribute("currentWord", game.getCurrentWord());
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
                loadSettings(session);
                try {
                    ArrayList<LanguageBean> languages = db.findLanguages();
                    session.setAttribute("languages", languages);
                } catch (NullPointerException e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }
                response.sendRedirect("settingsView.jsp");
                break;
            case "instructions":
                response.sendRedirect("instructionsView.jsp");
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

    private void loadSettings(HttpSession session) {
        UserBean userBean = (UserBean) session.getAttribute("userBean");
        SettingsBean settingsBean;
        if (userBean != null) {
            settingsBean = db.findUserSettings(userBean.getID());
            if (settingsBean != null) {
                session.setAttribute("settingsBean", settingsBean);
            } else {
                settingsBean = new SettingsBean();
                session.setAttribute("settingsBean", settingsBean);
            }
        } else {
            settingsBean = (SettingsBean) session.getAttribute("settingsBean");
            if (settingsBean == null)
                settingsBean = new SettingsBean();
            session.setAttribute("settingsBean", settingsBean);
        }
    }

    private SettingsBean preventNullSettingsBean(HttpSession session) {
        UserBean userBean = (UserBean) session.getAttribute("userBean");
        SettingsBean settingsBean;

        if (userBean != null) {
            settingsBean = db.findUserSettings(userBean.getID());
            if (settingsBean != null) {
                session.setAttribute("settingsBean", settingsBean);
            }
        } else {
            settingsBean = (SettingsBean) session.getAttribute("settingsBean");
        }

        if (settingsBean == null) {
            settingsBean = new SettingsBean();
            session.setAttribute("settingsBean", settingsBean);
            System.out.println("Set default settings from preventNullSettingsBean");
        }
        return settingsBean;
    }

    private void doInstructionsView(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
        response.sendRedirect("instructionsView.jsp");
    }

}