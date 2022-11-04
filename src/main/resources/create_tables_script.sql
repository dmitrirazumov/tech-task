CREATE TABLE gender
(
    id     SERIAL PRIMARY KEY,
    gender_name VARCHAR(16)
);

CREATE TABLE users
(
    username   VARCHAR(128) PRIMARY KEY,
    first_name VARCHAR(128)                       NOT NULL,
    last_name  VARCHAR(128)                       NOT NULL,
    birthday   DATE                               NOT NULL,
    email      VARCHAR(128)                       NOT NULL UNIQUE,
    gender     INT REFERENCES gender (id) NOT NULL
);