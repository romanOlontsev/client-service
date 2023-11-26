--liquibase formatted sql

--changeset admin:2
insert into security_app.addresses(address_name)
values ('http://localhost:8079');
insert into security_app.addresses(address_name)
values ('http://localhost:8080');
insert into security_app.addresses(address_name)
values ('http://localhost:8081');
insert into security_app.addresses(address_name)
values ('http://localhost:8082');
insert into security_app.addresses(address_name)
values ('http://localhost:8083');