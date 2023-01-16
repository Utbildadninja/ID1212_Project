<%--
  Created by IntelliJ IDEA.
  User: swedi
  Date: 2023-01-05
  Time: 21:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>TestView</title>
    <style>
        <%@include file="/WEB-INF/style.css" %>
    </style>
</head>
<body>
<div class="outerDiv">
    <div class="instructions-view">
        <h2>How To Play</h2>
        <div>
            <p>
                <i> In Own Words </i> is an engaging game of communication and verbal creativity. Whether you are having
                a
                party with friends, or a family evening, you will enjoy the simple yet surprisingly difficult gameplay.
            </p>
            <p>
                You play in teams, and take turns explaining the word on the screen without using the exact word. It is
                you
                teammates' job to guess witch word that you are describing. If they guess correctly, you press
                "correct". If
                you feel stuck, you can press "skip" and you get a new word. Try not to skip too many words, since it is
                more fun to challenge yourself and actually try to explain even the difficult words!
            </p>
            When the times runs out, you are not allowed to talk anymore. Your teammate gets a final guess, press
            "correct"
            or "skip" depending on the outcome, then the turn moves on to the next team.
            <p>
                This implementation is suited for playing in hotseat mode, that is, when everyone playing is in the same
                room. Pass the phone around so that the person explaining is the one holding it, and so that no other
                player
                can sneakpeak at the screen (seeing the word without getting it from the explanation ruins the fun).
            </p>
            <p> You can enter how long each round should be, and how many rounds should be played, on the Settings page
                from
                the Main menu. After all rounds are played, the score is displayed and the team with the most points win
                the
                game. Simple as that!
            </p>

            <form action="index.jsp">
                <button type="submit">Back to menu</button>
            </form>

        </div></div>
    </div>
</body>
</html>
