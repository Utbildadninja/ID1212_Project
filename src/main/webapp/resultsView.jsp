<%@ page import="kth.se.id1212.model.TeamBean" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    List<TeamBean> teamsPlaying = (List<TeamBean>) session.getAttribute("teamsPlaying");
%>
<html>
<head>
    <title>Results</title>
    <style>
        <%@include file="/WEB-INF/style.css" %>
    </style>
</head>
<body>
<div class="container">

    <h1>Game over!</h1>
    <%
        if (teamsPlaying != null) {
            for (TeamBean team : teamsPlaying) {
    %>
    <div class="resultsViewDiv">
        <h2>Team name: <%= team.getName() %>
        </h2>
        <h3>Score: <%= team.getScore() %>
        </h3>
        <h3>Correct guesses:</h3>
        <%
            for (String guess : team.getCorrectGuesses()) {
        %>
        <%=guess%>
        <br>
        <%
            }
        %>
    </div>
    <br>
    <%
            }
        }
    %>
    <form action="${pageContext.request.contextPath}/ControllerServlet" method="get">

        <input type="hidden" name="jspFile" value="<%= request.getRequestURI() %>">
        <button type="submit">Back to Menu</button>
    </form>
</div>
</body>
</html>
