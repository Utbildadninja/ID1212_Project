<%@ page import="java.util.List" %>
<%@ page import="kth.se.id1212.model.TeamBean" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    List<TeamBean> teamsPlaying = (List<TeamBean>) session.getAttribute("teamsPlaying");
%>
<html>
<head>
    <title>Setup</title>
    <style>
        <%@include file="/WEB-INF/style.css" %>
    </style>
</head>
<body>
<div class="outerDiv">
    <ul>
        <%
            if (teamsPlaying != null) {
                for (TeamBean teamBean : teamsPlaying) {
        %>
        <li>
            <%=teamBean.getName()%>
            <form action="${pageContext.request.contextPath}/ControllerServlet" method="get">
                <input type="hidden" name="action" value="remove">
                <input type="hidden" name="jspFile" value="<%= request.getRequestURI() %>">
                <input type="hidden" name="teamToRemove" value="<%=teamBean.getName()%>">
                <button type="submit">Remove</button>
            </form>
        </li>
        <%
                }
            }
        %>
    </ul>
    <form action="${pageContext.request.contextPath}/ControllerServlet" method="get">
        <label>
            <input type="text" name="team" placeholder="Enter team name">
        </label>
        <input type="hidden" name="action" value="add">
        <input type="hidden" name="jspFile" value="<%= request.getRequestURI() %>">
        <button type="submit">Add Team</button>
    </form>
    <form action="${pageContext.request.contextPath}/ControllerServlet" method="get">
        <input type="hidden" name="action" value="start">
        <input type="hidden" name="jspFile" value="<%= request.getRequestURI() %>">
        <button type="submit">Start Game</button>
    </form>
</div>
</body>
</html>
