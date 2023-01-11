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
<div class="outerDiv">

    All of the scores, teams, words etc
    <%
        if (teamsPlaying != null) {
            for (TeamBean team : teamsPlaying) {
    %>
    <p>Team name: <%= team.getName() %>, Score: <%= team.getScore() %>
    </p>
    <%
        for (String guess : team.getCorrectGuesses()) {
    %>
    <%= guess %>
    <br>
    <%
                }
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
