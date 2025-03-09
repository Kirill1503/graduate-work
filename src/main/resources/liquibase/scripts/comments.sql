-- liquibase formatted sql

-- changeset kkatyshev:3

CREATE TABLE comments
(
    id         BIGSERIAL PRIMARY KEY,
    author_id  BIGINT    NOT NULL,
    ad_id      BIGSERIAL     NOT NULL,
    text       VARCHAR(1000) NOT NULL,
    created_at TIMESTAMP     NOT NULL,
    CONSTRAINT fk_comment_author FOREIGN KEY (author_id) REFERENCES users (id),
    CONSTRAINT fk_comment_ad FOREIGN KEY (ad_id) REFERENCES ads (id)
);

CREATE INDEX comment_author_index ON comments (author_id);
CREATE INDEX comment_ad_index ON comments (ad_id);
