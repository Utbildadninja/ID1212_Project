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
        else if (jspFile.endsWith("/testView.jsp")) {
            doTestView(request, response, session);
        }
    }

    private void doTestView(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
        game.newGame();
        game.setCurrentWord();
        String currentWord = game.getCurrentWord();
        System.out.println("Current word: " + currentWord);


        response.sendRedirect("testView.jsp");
    }



//    public List<WordBean> callPremiumAPI() throws IOException {
//
//        return apiCalls.getNewArrayPremium();
//    }


}