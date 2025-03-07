-- liquibase formatted sql

-- changeset kkatyshev:1

CREATE TABLE users
(
    id         BIGSERIAL PRIMARY KEY,
    email      VARCHAR(255) UNIQUE,
    password   VARCHAR(255)        NOT NULL,
    username   VARCHAR(100) UNIQUE NOT NULL,
    first_name VARCHAR(100),
    last_name  VARCHAR(100),
    phone      VARCHAR(20),
    image      VARCHAR(255),
    role       VARCHAR(50)
);

CREATE INDEX username_index ON users (username);
CREATE INDEX email_index ON users (email);
