package kth.se.id1212.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import kth.se.id1212.model.ApiCalls;
import kth.se.id1212.model.Game;
import kth.se.id1212.model.WordBean;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


@WebServlet(name = "ControllerServlet", value = "/ControllerServlet")
public class ControllerServlet extends HttpServlet {
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
        else if (jspFile.endsWith("/setUpView.jsp")) {
            doSetUpView(request, response, session);
        }
        else if (jspFile.endsWith("/testView.jsp")) {
            doTestView(request, response, session);
        }
    }

    private void doGameView(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
        String action = request.getParameter("action");
        if (action.equals("correct")) {
            System.out.println("Action was: " + action);
            game.correct();
        } else if (action.equals("skip")) {
            System.out.println("Action was: " + action);
            game.skip();
        } else System.out.println("Action was " + action + ". That was unexpected");

        String currentWord = game.getCurrentWord();
        System.out.println("Setting current word from doGameView: " + currentWord);

        session.setAttribute("currentWord", currentWord);

        response.sendRedirect("gameView.jsp");
    }

    private void doSetUpView(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
        game.newGame();
        String currentWord = game.getCurrentWord();
        session.setAttribute("currentWord", currentWord);
        response.sendRedirect("gameView.jsp");
    }

    private void doTestView(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {

        response.sendRedirect("testView.jsp");
    }

}