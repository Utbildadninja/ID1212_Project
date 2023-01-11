        <%@ page import="kth.se.id1212.model.UserBean" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Settings</title>
    <style>
        <%@include file="/WEB-INF/style.css" %>
    </style>
</head>
<body>
<%
    //    int roundTime = (int) session.getAttribute("roundTimeSlider");
    UserBean userBean = (UserBean) session.getAttribute("userBean");
    String username = "default";
    if (userBean != null) {
        username = userBean.getUsername();
    }
%>


<div class="settingsViewDiv">
    Choose API, Database, round time etc
    Logged in as: <%=username%>

    <form action="${pageContext.request.contextPath}/ControllerServlet" method="get">
        <label for="roundTimeSlider">Round time:</label>
        <input name="roundTimeSlider" type="range" min="10" max="120" value="30" step="5" class="slider"
               id="roundTimeSlider" oninput="roundTimeOutput.value = roundTimeSlider.value">
        <output name="roundTimeOutput" id="roundTimeOutput">30</output>
        <br>
        <label for="numberOfRounds">Number of Rounds:</label>
        <input type="range" id="numberOfRounds" name="numberOfRounds" min="1" max="5" value="3" step="1"
               oninput="numberOfRoundsValue.value=numberOfRounds.value">
        <output name="numberOfRoundsValue" id="numberOfRoundsValue">3</output>
        <br>
        Choose source of words:
        <br>
        <label for="freeApi">Free API</label>
        <input type="radio" id="freeApi" name="wordSource" value="free" checked>

        <label for="premiumApi">Premium API</label>
        <input type="radio" id="premiumApi" name="wordSource" value="premium">

        <input type="hidden" name="action" value="submit">
        <input type="hidden" name="jspFile" value="<%=request.getRequestURI()%>">
        <br>
        <button type="submit">Submit</button>
    </form>
</div>

</body>
</html>
