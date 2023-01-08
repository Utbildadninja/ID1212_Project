<%--
  Created by IntelliJ IDEA.
  User: swedi
  Date: 2023-01-05
  Time: 21:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>TestView</title>
</head>
<body>
WRITE SOMETHING PLZ
<form action="${pageContext.request.contextPath}/ControllerServlet" method="get">

  <input type="hidden" name="jspFile" value="<%= request.getRequestURI() %>">
  <button type="submit">Send HTTP Request</button>
</form>

</body>
</html>
