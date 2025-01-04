-- liquibase formatted sql

CREATE TABLE state_limits
(
    state VARCHAR(5)       NOT NULL,
    min   DOUBLE PRECISION NOT NULL,
    max   DOUBLE PRECISION,
    CONSTRAINT pk_state_limits PRIMARY KEY (state)
);