CREATE EXTENSION IF NOT EXISTS CITEXT;

CREATE TABLE IF NOT EXISTS users (
  login    citext PRIMARY KEY ,
  email    citext UNIQUE,
  password TEXT,
  score    INT DEFAULT 0
);