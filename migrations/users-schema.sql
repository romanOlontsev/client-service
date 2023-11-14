--liquibase formatted sql

--changeset admin:1
create schema if not exists users_app;
create table if not exists users_app.header
(
    id              bigserial        not null primary key,
    header_name     varchar(255)
);
create table if not exists users_app.required_fields
(
    header_validation_id bigint         not null references users_app.header,
    required_fields      varchar(255)
);
create table users_app.account (
    id                      bigserial       not null primary key,
    bank_id                 bigint,
    birth_date              date,
    birthplace              varchar(255),
    email                   varchar(255),
    first_name              varchar(255),
    last_name               varchar(255),
    middle_name             varchar(255),
    passport                varchar(255),
    phone_number            varchar(255),
    registration_address    varchar(255),
    residential_address     varchar(255)
);
