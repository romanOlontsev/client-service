--liquibase formatted sql

--changeset admin:1
insert into security_app.users(login, password)
values ('admin', '$2a$10$qe9.MTY8Jf/0Bwkm0UgBIOoi3BxUL/1e2rU.tVOBKP81NLeU3t9Q.');