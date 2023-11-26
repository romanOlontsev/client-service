--liquibase formatted sql

--changeset admin:1
create schema if not exists security_app;
create table if not exists security_app.addresses
(
    id           bigserial    not null primary key,
    address_name varchar(255) not null
);