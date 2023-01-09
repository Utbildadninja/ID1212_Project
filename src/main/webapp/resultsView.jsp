<%@ page import="kth.se.id1212.model.TeamBean" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Results</title>
    <style><%@include file="/WEB-INF/style.css"%></style>
</head>
<body>
<%
    List<TeamBean> teamsPlaying = (List<TeamBean>) session.getAttribute("teamsPlaying");
%>

All of the scores, teams, words etc
<%
    if (teamsPlaying != null) {
        for (TeamBean team : teamsPlaying) {
%>
<p>Team name: <%= team.getName() %>, Score: <%= team.getScore() %></p>
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
</body>
</html>
