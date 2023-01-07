<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Med egna ord</title>
</head>
<body>
<%
    String currentWord = session.getAttribute("currentWord").toString();
    int score = (int) session.getAttribute("score");
%>
<p>Current word: <%=currentWord%></p>
<p>Current score: <%=score%></p>

<%--TODO Hide regular buttons when timer is up, also um, implement the timer--%>

<form action="${pageContext.request.contextPath}/ControllerServlet" method="get">
    <input type="hidden" name="action" value="correct">
    <input type="hidden" name="jspFile" value="<%= request.getRequestURI() %>">
    <button type="submit">Correct</button>
</form>
<form action="${pageContext.request.contextPath}/ControllerServlet" method="get">
    <input type="hidden" name="action" value="skip">
    <input type="hidden" name="jspFile" value="<%= request.getRequestURI() %>">
    <button type="submit">Skip</button>
</form>
<div>Under construction obviously, for testing</div>
<form action="${pageContext.request.contextPath}/ControllerServlet" method="get">
    <input type="hidden" name="action" value="finalGuessSkip">
    <input type="hidden" name="jspFile" value="<%= request.getRequestURI() %>">
    <button type="submit">FinalGuessSkip</button>
</form>
<form action="${pageContext.request.contextPath}/ControllerServlet" method="get">
    <input type="hidden" name="action" value="results">
    <input type="hidden" name="jspFile" value="<%= request.getRequestURI() %>">
    <button type="submit">Results</button>
</form>
</body>
</html>
