<%--
  Created by IntelliJ IDEA.
  User: swedi
  Date: 2023-01-07
  Time: 20:28
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Setup</title>
</head>
<body>
<div>Pick a team name etc</div>
<form action="${pageContext.request.contextPath}/ControllerServlet" method="get">

  <input type="hidden" name="jspFile" value="<%= request.getRequestURI() %>">
  <button type="submit">Start Game</button>
</form>
</body>
</html>
