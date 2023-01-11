<%@ page import="kth.se.id1212.model.TeamBean" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%TeamBean nextTeamBean = (TeamBean) session.getAttribute("nextTeamBean");%>
<html>
<head>
    <title>Med egna ord</title>
    <style>
        <%@include file="/WEB-INF/style.css" %>
    </style>
</head>
<body>
<div class="outerDiv">

    <h1>Get ready!</h1>
    <p>Team <%=nextTeamBean.getName()%> you're up!</p>
    <p>Make sure the person guessing can't see the screen!</p>

    <form action="${pageContext.request.contextPath}/ControllerServlet" method="get">
        <input type="hidden" name="jspFile" value="<%= request.getRequestURI() %>">
        <button type="submit">Next Round</button>
    </form>
</div>
</body>
</html>
