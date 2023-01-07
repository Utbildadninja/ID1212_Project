<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Med egna ord</title>
</head>
<body>
<%
    String currentWord = session.getAttribute("currentWord").toString();
%>
<p>Current word: <%=currentWord%></p>

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
</body>
</html>
