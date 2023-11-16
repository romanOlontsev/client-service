--liquibase formatted sql

--changeset admin:6
create schema if not exists tariffs_app;
create table if not exists tariffs_app.tariff
(
    id              uuid                not null primary key,
    name            varchar(255),
    start_date      timestamp(6),
    end_date        timestamp(6),
    description     text,
    rate            double precision,
    version         bigint
);
create table if not exists tariffs_app.revinfo
(
    rev         serial      primary key,
    revtstmp    timestamp(6)
);
create table if not exists tariffs_app.tariff_aud
(
    id              uuid        not null,
    rev             integer     not null,
    revtype         smallint,
    name            varchar(255),
    start_date      timestamp(6),
    end_date        timestamp(6),
    description     text,
    rate            double precision,
    version         bigint,
    primary key (rev, id),
    foreign key (rev) references tariffs_app.revinfo(rev)
);