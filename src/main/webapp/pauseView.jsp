<%@ page import="kth.se.id1212.model.TeamBean" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
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
    <div class="container">

        <c:choose>
            <c:when test="${sessionScope.gameOver == true}">
                That was the last round!

                <form action="${pageContext.request.contextPath}/ControllerServlet" method="get">
                    <input type="hidden" name="jspFile" value="<%= request.getRequestURI() %>">
                    <button class="home-view-btn" type="submit">Show Results</button>
                </form>
            </c:when>
            <c:otherwise>
                <h1>Get ready!</h1>
                <p>Team <%=nextTeamBean.getName()%> you're up!</p>
                <p>Make sure the person guessing can't see the screen!</p>

                <form action="${pageContext.request.contextPath}/ControllerServlet" method="get">
                    <input type="hidden" name="jspFile" value="<%= request.getRequestURI() %>">
                    <button class="home-view-btn" type="submit">Next Round</button>
                </form>
            </c:otherwise>
        </c:choose>
    </div>
</div>
</body>
</html>
