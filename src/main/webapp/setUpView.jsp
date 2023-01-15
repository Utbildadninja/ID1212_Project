<%@ page import="java.util.List" %>
<%@ page import="kth.se.id1212.model.TeamBean" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    List<TeamBean> teamsPlaying = (List<TeamBean>) session.getAttribute("teamsPlaying");
    boolean disabled = true;
    if (teamsPlaying != null)
        disabled = teamsPlaying.size() == 0;
%>
<html>
<head>
    <title>Setup</title>
    <style>
        <%@include file="/WEB-INF/style.css" %>
    </style>
</head>
<body>
<div class="outerDiv">
    <div class="container">
        <form action="${pageContext.request.contextPath}/ControllerServlet" method="get">
            <input type="hidden" name="action" value="start">
            <input type="hidden" name="jspFile" value="<%= request.getRequestURI() %>">
            <button class="home-view-btn" type="submit" <%= disabled ? "disabled" : "" %>>Start Game</button>
        </form>
        <form action="${pageContext.request.contextPath}/ControllerServlet" method="get">
            <label>
                <input type="text" name="team" placeholder="Enter team name">
            </label>
            <input type="hidden" name="action" value="add">
            <input type="hidden" name="jspFile" value="<%= request.getRequestURI() %>">
            <button class="squareButton" type="submit">Add Team</button>
        </form>

        <table>
            <tbody>
            <%
                if (teamsPlaying != null) {
                    for (TeamBean teamBean : teamsPlaying) {
            %>
                <tr>
                    <td>
                        <form action="${pageContext.request.contextPath}/ControllerServlet" method="get">
                            <input type="hidden" name="action" value="remove">
                            <input type="hidden" name="jspFile" value="<%= request.getRequestURI() %>">
                            <input type="hidden" name="teamToRemove" value="<%=teamBean.getName()%>">
                            <button class="removeButton" type="submit">X</button>
                        </form>
                    </td>
                    <td>
                        <%=teamBean.getName()%>
                    </td>
                    <%
                            }
                        }
                    %>
                </tr>
            </tbody>
        </table>
    </div>
</div>
</body>
</html>
