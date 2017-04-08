CREATE TABLE greetings (
  id serial PRIMARY KEY,
  language varchar(256) NOT NULL,
  content varchar(256) NOT NULL,
  create_date timestamp NOT NULL default now()
);