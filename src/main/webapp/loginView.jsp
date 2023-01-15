<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
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
    <c:if test="${not empty errorMessage}">
        <p class="errorMessage">${errorMessage}</p>
    </c:if>
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

    <br>
    Are you not yet part of our growing empire?
    <br>
    <form action="${pageContext.request.contextPath}/ControllerServlet" method="get">
        <label>
            <input type="hidden" name="action" value="createAccount">
            <input type="hidden" name="jspFile" value="<%= request.getRequestURI() %>">
            <button class="signUpButton" type="submit">Sign up now!</button>
        </label>
    </form>

</div>
</body>
</html>
