-- liquibase formatted sql

-- changeset kkatyshev:2

CREATE TABLE ads
(
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(100),
    price INTEGER,
    description VARCHAR(400),
    image TEXT,
    author_id   BIGINT NOT NULL,
    CONSTRAINT fk_author FOREIGN KEY (author_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE INDEX author_index ON ads (author_id);
