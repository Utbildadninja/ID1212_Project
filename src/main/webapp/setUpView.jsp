<%@ page import="java.util.List" %>
<%@ page import="kth.se.id1212.model.TeamBean" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Setup</title>
</head>
<body>
<%
    List<TeamBean> teamsPlaying = (List<TeamBean>) session.getAttribute("teamsPlaying");
%>


<div>Pick a team name etc</div>
<ul>
    <%if(teamsPlaying != null){
        for (TeamBean teamBean : teamsPlaying) {
    %>
    <li><%=teamBean.getName()%></li>
    <%
            }
        }
    %>
</ul>
<form action="${pageContext.request.contextPath}/ControllerServlet" method="get">
    <label>
        <input type="text" name="team" value="teamName">
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
</body>
</html>
