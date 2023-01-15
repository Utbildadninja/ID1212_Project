# ID1212 Course Project - In Own Words (Med egna ord)

This app was created as part of the course work for the KTH Royal Institue of Technology course ID1212 Network Programming. 
The app aims to be a digital clone of the board game "In Other Words"/"Med andra ord".

## Description
The purpose of the course project (as we understood it) was to develope for the web through backend methods. Instead of using eg JavaScript and Firebase, 
we have used Java with HttpServlet classes and .jsp pages for the view, and the app runs on a Tomcat server. 
The code is structured in accordance with the MVC and Layer patterns. An Apache Derby database holds accounts, settings and word information.

The concept of the actual game is that you play in teams of at least two. The teammember take turn in being the explainer and the guesser. 
The explainer get a word on the screen that they have to explain to their teammeber without saying that exact work - ie, in other words. Within a certain time frame that can be chosen by the user, they want to get as many words as possible correct within the team, before the turn passes to the next team. The game is played in between two and five rounds, before a winner is decided based upon the number of words that were correctly guessed.


## Dependencies
The app was created as a maven project with JDK17, Jakarta 10,TomCat 10.1.4 and Derby 10.16.
You need to install Derby and create a database according to the create_database.sql file in the repository.
You will need to keep track of your database username and password, and store these as environment variables in order to run the program
(they are loaded in the connectToDB method).
You will also need TomCat and Java and Jakarta. 

The program can be run completely without connecting to the DB, but then no account features will be available. Netiher will playing with words from the database be, so the user is limited to playing with words fetched from the API. In order to use the Premium API, you will need an API key from the Wordnik API (https://api.wordnik.com/v4/) and store it as an environment variable (or edit the code and put it there directly). There is also a testing API (https://random-word-api.herokuapp.com/) that has a free unlimited amount of API calls.


## Authors
Niclas 
Tove 
Contributors names and contact info
