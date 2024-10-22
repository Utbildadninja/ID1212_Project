<%@ page import="kth.se.id1212.model.TeamBean" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%
    String currentWord = session.getAttribute("currentWord").toString();
    int score = (int) session.getAttribute("score");
    int timeLeft = (int) session.getAttribute("timeLeft");
    TeamBean teamBean = (TeamBean) session.getAttribute("currentTeamBean");
%>

<html>
<head>
    <title>Med egna ord</title>
    <style>
        <%@include file="/WEB-INF/style.css" %>
        <%@include file="/WEB-INF/buttons.css" %>
    </style>
</head>
<body>
<div class="outerDiv">
    <noscript>JavaScript is disabled. Website might not work as intended.</noscript>

    <h1>
        <c:choose>
            <c:when test="${timeLeft <= 0}">
                Time's up!
            </c:when>
            <c:otherwise>
                <span id="timer">${timeLeft}</span> seconds
            </c:otherwise>
        </c:choose>
    </h1>

    <div class="gameViewWord">
        <h2 class="gameActualWord"><%=currentWord%>
        </h2>
    </div>

    <c:if test="${timeLeft <= 0}">
        <p class="timeUpInfo">No more talking! Make a final guess!
        </p>
    </c:if>


    <script>
        let timeLeft = <%=timeLeft%>;
        let timer = setInterval(function () {
            if (timeLeft > 0) {
                timeLeft--;
            }
            document.getElementById("timer").innerHTML = timeLeft;
            if (timeLeft <= 0) {
                clearInterval(timer);
                document.getElementById("timerUpForm").submit();
            }
        }, 1000);
    </script>

    <form action="${pageContext.request.contextPath}/ControllerServlet" method="get">
        <input type="hidden" name="action" value="correct">
        <input type="hidden" name="jspFile" value="<%= request.getRequestURI() %>">
        <button class="correctButton" type="submit">Correct</button>
    </form>
    <form action="${pageContext.request.contextPath}/ControllerServlet" method="get">
        <input type="hidden" name="action" value="skip">
        <input type="hidden" name="jspFile" value="<%= request.getRequestURI() %>">
        <button class="skipButton" type="submit">Skip</button>
    </form>
    <h3>Current score: <%=score%>
    </h3>
    <h3>Current team: <%=teamBean.getName()%>
    </h3>
</div>
<form id="timerUpForm" action="${pageContext.request.contextPath}/ControllerServlet" method="get">
    <input type="hidden" name="action" value="timer_up">
    <input type="hidden" name="jspFile" value="<%= request.getRequestURI() %>">
</form>
</body>
</html>
