<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Results</title>
</head>
<body>
All of the scores, teams, words etc

<form action="${pageContext.request.contextPath}/ControllerServlet" method="get">

    <input type="hidden" name="jspFile" value="<%= request.getRequestURI() %>">
    <button type="submit">Back to Menu</button>
</form>
</body>
</html>
