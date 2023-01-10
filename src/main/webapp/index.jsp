<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Med egna ord</title>
    <style>
        <%@include file="/WEB-INF/style.css" %>
    </style>
</head>
<body>
<div class="outerDiv">

    <div class="home-view-game-display-box">

        <div class="home-view-btn-container">
            <h1>Med egna ord</h1>
            <form action="${pageContext.request.contextPath}/ControllerServlet" method="get">
                <input type="hidden" name="action" value="play">
                <input type="hidden" name="jspFile" value="<%= request.getRequestURI() %>">
                <button class="home-view-btn" type="submit">PLAY</button>
            </form>
            <form action="${pageContext.request.contextPath}/ControllerServlet" method="get">
                <input type="hidden" name="action" value="settings">
                <input type="hidden" name="jspFile" value="<%= request.getRequestURI() %>">
                <button class="home-view-btn" type="submit">SETTINGS</button>
            </form>
            <form action="${pageContext.request.contextPath}/ControllerServlet" method="get">
                <input type="hidden" name="action" value="test">
                <input type="hidden" name="jspFile" value="<%= request.getRequestURI() %>">
                <button class="home-view-btn" type="submit">TESTING</button>
            </form>
            <form action="${pageContext.request.contextPath}/ControllerServlet" method="get">
                <input type="hidden" name="action" value="login">
                <input type="hidden" name="jspFile" value="<%= request.getRequestURI() %>">
                <button class="home-view-btn" type="submit">LOG IN</button>
            </form>
        </div>
    </div>
</div>

</body>
</html>