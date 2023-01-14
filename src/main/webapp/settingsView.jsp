<%@ page import="kth.se.id1212.model.UserBean" %>
<%@ page import="kth.se.id1212.model.SettingsBean" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="kth.se.id1212.model.LanguageBean" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%--<%@ page session="true" %>--%>

<%
    ArrayList<LanguageBean> languages = (ArrayList) session.getAttribute("languages");
    SettingsBean settingsBean = (SettingsBean) session.getAttribute("settingsBean");
    UserBean userBean = (UserBean) session.getAttribute("userBean");
    String username = "default";
    if (userBean != null) {
        username = userBean.getUsername();
    }

    int secondsPerRound = settingsBean.getSecondsPerRound();
    int roundsPerGame = settingsBean.getRoundsPerGame();

    String sneaky;
    if (userBean != null) {
        sneaky = "";
    } else sneaky = "hidden";

    String reverseSneaky;
    if (userBean == null) {
        reverseSneaky = "";
    } else reverseSneaky = "hidden";
%>

<html>
<head>
    <title>Settings</title>
    <style>
        <%@include file="/WEB-INF/style.css" %>
    </style>
</head>
<body>

<div class="outerDiv">
    <noscript>JavaScript is disabled. Website might not work as intended.<br></noscript>
    <h2>Settings</h2>

    <p id=<%=reverseSneaky%>>Not logged in</p>
    <p id=<%=sneaky%>>Logged in as: <%=username%></p>

    <form action="${pageContext.request.contextPath}/ControllerServlet" method="get">
        <label for="roundTimeSlider">Round time:</label>
        <input name="roundTimeSlider" type="range" min="10" max="120" value="<%=secondsPerRound%>" step="5" class="slider"
               id="roundTimeSlider" oninput="roundTimeOutput.value = roundTimeSlider.value">
        <output name="roundTimeOutput" id="roundTimeOutput"><%=secondsPerRound%></output>
        <br>
        <label for="numberOfRounds">Number of Rounds:</label>
        <input type="range" id="numberOfRounds" name="numberOfRounds" min="1" max="5" value="<%=roundsPerGame%>" step="1"
               oninput="numberOfRoundsValue.value=numberOfRounds.value">
        <output name="numberOfRoundsValue" id="numberOfRoundsValue"><%=roundsPerGame%></output>
        <br>
        Source of words: <%=settingsBean.getLanguageName()%>
        <br>


        <label>
            <select name="language">
                <option value="" disabled selected>Select a Language</option>
                <option value="Test_API">Test API</option>
                <option value="English_API">English API</option>
                <%
                    if(languages != null){
                        for (LanguageBean language : languages) {
                %>
                <option value="<%=language.getLanguageID()%>"><%=language.getLanguageName()%></option>
                <%
                        }
                    }
                %>
            </select>
        </label>
        <br>

        Tramstesting
        <br>
        <c:forEach var="language" items="${languages}">
            <div>Name: ${language.languageName}</div>
        </c:forEach>
        <br>


        <input type="hidden" name="action" value="submit">
        <input type="hidden" name="jspFile" value="<%=request.getRequestURI()%>">
        <button type="submit">Submit</button>
    </form>

    <form action="index.jsp">
        <button type="submit">Back to menu</button>
    </form>

</div>

</body>
</html>
