<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Log In</title>
    <style>
        <%@include file="/WEB-INF/style.css" %>
    </style>
</head>
<body>
<div class="outerDiv">
    Please enter credentials:
    <br>
    <form action="${pageContext.request.contextPath}/ControllerServlet" method="post">
        <label>
            <input type="hidden" name="action" value="login">
            <input type="text" name="username" placeholder="Enter username">
            <br>
            <input type="password" name="password" placeholder="Enter password">
            <input type="hidden" name="jspFile" value="<%= request.getRequestURI() %>">
            <br>
            <button type="submit">Submit</button>
        </label>
    </form>

    <form action="${pageContext.request.contextPath}/ControllerServlet" method="get">
        <label>
            <input type="hidden" name="action" value="createAccount">
            <input type="hidden" name="jspFile" value="<%= request.getRequestURI() %>">
            <button type="submit">Create account</button>
        </label>
    </form>


</div>
</body>
</html>
