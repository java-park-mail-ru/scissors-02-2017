CREATE EXTENSION IF NOT EXISTS CITEXT;

CREATE TABLE IF NOT EXISTS users (
  id       SERIAL PRIMARY KEY,
  login    citext UNIQUE,
  email    citext UNIQUE,
  password TEXT,
  score    INT
);