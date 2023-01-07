<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Settings</title>
</head>
<body>
Choose API, Database, round time etc
<form action="${pageContext.request.contextPath}/ControllerServlet" method="get">
    <input type="hidden" name="action" value="skip">
    <input type="hidden" name="jspFile" value="<%= request.getRequestURI() %>">
    <button type="submit">Submit</button>
</form>
</body>
</html>
