<%@ page import="kth.se.id1212.model.TeamBean" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Med egna ord</title>
</head>
<body>
<%
    String currentWord = session.getAttribute("currentWord").toString();
    int score = (int) session.getAttribute("score");
    int timeLeft = (int) session.getAttribute("timeLeft");
    TeamBean teamBean = (TeamBean) session.getAttribute("currentTeamBean");
%>
<p>Current word: <%=currentWord%></p>
<p>Current score: <%=score%></p>
<p>Current team: <%=teamBean.getName()%></p>

<p>Time remaining: <span id="timer"><%=timeLeft%></span> seconds</p>

<script>
    let timeLeft = <%=timeLeft%>;
    let timer = setInterval(function() {
        if (timeLeft > 0) {
            timeLeft--;
        }
        document.getElementById("timer").innerHTML = timeLeft;
        if (timeLeft <= 0) {
            clearInterval(timer);
            // Take action
        }
    }, 1000);
</script>

<form action="${pageContext.request.contextPath}/ControllerServlet" method="get">
    <input type="hidden" name="action" value="correct">
    <input type="hidden" name="jspFile" value="<%= request.getRequestURI() %>">
    <button id ="correct" type="submit">Correct</button>
</form>
<form action="${pageContext.request.contextPath}/ControllerServlet" method="get">
    <input type="hidden" name="action" value="skip">
    <input type="hidden" name="jspFile" value="<%= request.getRequestURI() %>">
    <button type="submit">Skip</button>
</form>
<div>Under construction obviously, for testing</div>
<%--<form action="${pageContext.request.contextPath}/ControllerServlet" method="get">--%>
<%--    <input type="hidden" name="action" value="finalGuessSkip">--%>
<%--    <input type="hidden" name="jspFile" value="<%= request.getRequestURI() %>">--%>
<%--    <button type="submit">FinalGuessSkip</button>--%>
<%--</form>--%>
<%--<form action="${pageContext.request.contextPath}/ControllerServlet" method="get">--%>
<%--    <input type="hidden" name="action" value="results">--%>
<%--    <input type="hidden" name="jspFile" value="<%= request.getRequestURI() %>">--%>
<%--    <button type="submit">Results</button>--%>
<%--</form>--%>
</body>
</html>
