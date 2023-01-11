<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Create Account</title>
    <style>
        <%@include file="/WEB-INF/style.css" %>
    </style>
</head>
<body>
<div class="outerDiv">
    Enter new account credentials:
    <br>
    <form action="${pageContext.request.contextPath}/ControllerServlet" method="post">
        <label>
            <input type="hidden" name="action" value="createAccount">
            <input type="text" name="username" placeholder="Choose username">
            <br>
            <input type="password" name="password" placeholder="Choose password">
            <br>
            <input type="password" name="confirm_password" placeholder="Confirm password">
            <input type="hidden" name="jspFile" value="<%= request.getRequestURI() %>">
            <br>
            <button type="submit">Submit</button>
        </label>
    </form>
</div>

</body>
</html>
