--liquibase formatted sql

--changeset admin:1
create schema if not exists security_app;
create table if not exists security_app.users
(
    id       bigserial    not null primary key,
    login    varchar(255) not null,
    password varchar(255) not null
);