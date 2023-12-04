--liquibase formatted sql

--changeset admin:3
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

insert into security_app.addresses(address_name)
values ('http://auth-service:8079');
insert into security_app.addresses(address_name)
values ('http://products-service:8081');
insert into security_app.addresses(address_name)
values ('http://tariffs-service:8082');
insert into security_app.addresses(address_name)
values ('http://backend-service:8083');