<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Med egna ord</title>
</head>
<body>
<h1>Get ready!</h1>
<p>Team *Insert next team* you're up!</p>
<p>Make sure the person guessing can't see the screen!</p>
<p>PS. Does nothing at all yet</p>

<form action="${pageContext.request.contextPath}/ControllerServlet" method="get">
    <input type="hidden" name="jspFile" value="<%= request.getRequestURI() %>">
    <button type="submit">Next Round</button>
</form>
</body>
</html>
