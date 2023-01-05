package kth.se.id1212.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import kth.se.id1212.model.ApiCalls;

import java.io.IOException;


@WebServlet(name = "ControllerServlet", value = "/ControllerServlet")
public class ControllerServlet extends HttpServlet {
    ApiCalls apiCalls = new ApiCalls();


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String apiResponse = apiCalls.getNewArrayFree();
        //String apiResponse = apiCalls.getNewArrayPremium();
        // Process the API response
        System.out.println("API response from Controller: " + apiResponse);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }


}