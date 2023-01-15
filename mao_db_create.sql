
-- select a username and password.
-- CONNECT 'jdbc:derby:maoDB;create=true;user=user;password=password';
--     when connecting in the future, remove the create=true property
-- CONNECT 'jdbc:derby:maoDB;user=user;password=password';

CREATE TABLE users (
	id INT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
	username VARCHAR(32) UNIQUE NOT NULL,
	password VARCHAR(32) NOT NULL,
	admin BOOLEAN NOT NULL,
	gamesPlayed INT
);

-- ENUM table
CREATE TABLE languages (
    id INT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
    languageName VARCHAR(32) UNIQUE NOT NULL
);

CREATE TABLE user_settings(
    user_id INT,
    language_id INT,
    secondsPerRound INT,
    roundsPerGame INT
);
ALTER TABLE user_settings ADD CONSTRAINT FK_user_settings_0 FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE;
ALTER TABLE user_settings ADD CONSTRAINT FK_user_settings_1 FOREIGN KEY (language_id) REFERENCES languages (id);

CREATE TABLE words (
	id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
	language_id INT REFERENCES languages (id),
	word VARCHAR(64) NOT NULL,
	clue VARCHAR(256),
	correctlyGuessed INT,
	skipped INT,
	flags INT
);

CREATE TABLE reports (
    id INT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
    timeOfReport TIMESTAMP NOT NULL,
    reportingUser INT NOT NULL,
    reportedWord INT NOT NULL,
    reason VARCHAR(256)
);

ALTER TABLE reports ADD CONSTRAINT FK_reports_0 FOREIGN KEY (reportingUser) REFERENCES users (id);
ALTER TABLE reports ADD CONSTRAINT FK_reports_1 FOREIGN KEY (reportedWord) REFERENCES words (id);

-- Run the following to remove all tables in the database
-- DROP TABLE reports;
-- DROP TABLE user_settings;
-- DROP TABLE users;
-- DROP TABLE words;
-- DROP TABLE languages;