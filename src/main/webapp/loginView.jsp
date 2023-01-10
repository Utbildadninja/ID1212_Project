
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Log In</title>
    <style>
        <%@include file="/WEB-INF/style.css" %>
    </style>
</head>
<body>
Please enter credentials

<form action="${pageContext.request.contextPath}/ControllerServlet" method="post">Sign in: <label>
    <input type="text" name="username">
    <input type="password" name="password">
    <input type="hidden" name="jspFile" value="<%= request.getRequestURI() %>">
    <button type="submit">Submit</button>
</label></form>
</body>
</html>
