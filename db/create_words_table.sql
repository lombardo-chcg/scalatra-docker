create table words (
    ID serial PRIMARY KEY,
    word varchar(256) NOT NULL,
    canonical_word varchar(256) NOT NULL,
    points int NOT NULL,
    create_date timestamp NOT NULL default now()
);