--liquibase formatted sql

--changeset admin:7
create schema if not exists products_app;
create table if not exists products_app.product
(
    id              uuid not null primary key,
    name            varchar(255),
    type            varchar(4) check (type = 'LOAN' or type = 'CARD'),
    start_date      timestamp(6),
    end_date        timestamp(6),
    description     text,
    tariff          uuid,
    author          uuid,
    product_version bigint,
    tariff_version  bigint
);
create table if not exists products_app.revinfo
(
    rev      serial primary key,
    revtstmp timestamp(6)
);
create table if not exists products_app.product_aud
(
    id          uuid    not null,
    rev         integer not null,
    revtype     smallint,
    name        varchar(255),
    type        varchar(4),
    start_date  timestamp(6),
    end_date    timestamp(6),
    description text,
    tariff      uuid,
    author      uuid,
    product_version bigint,
    tariff_version  bigint,
    primary key (rev, id),
    foreign key (rev) references products_app.revinfo (rev)
)