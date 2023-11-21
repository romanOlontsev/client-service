--liquibase formatted sql

--changeset admin:5
insert into tariffs_app.tariff(id, name, start_date, end_date, description, rate, version)
values ('a32b8ba4-2dce-437c-ae94-34563b7ada43', 'tariff_1', '2023-11-21T12:40:40.0Z', '2023-11-22T12:40:40.0Z', 'description', 123.321, 2);
insert into tariffs_app.tariff(id, name, start_date, end_date, description, rate, version)
values ('5d033d70-712e-4bb9-abae-ba63f87a0b98', 'tariff_2', '2023-10-21T12:40:40.0Z', '2023-10-22T12:40:40.0Z', 'description_2', 456.654, 1);

insert into tariffs_app.revinfo(revtstmp) values ('2023-11-21T12:40:40.0Z');
insert into tariffs_app.revinfo(revtstmp) values ('2023-11-21T12:45:45.0Z');

insert into tariffs_app.revinfo(revtstmp) values ('2023-10-21T12:42:42.0Z');

insert into tariffs_app.tariff_aud(id, rev, revtype, name, start_date, end_date, description, rate, version)
values ('a32b8ba4-2dce-437c-ae94-34563b7ada43', 1, 0, 'tariff_1', '2023-11-21T12:40:40.0Z', '2023-11-22T12:40:40.0Z', 'desc', 123.321, 1);
insert into tariffs_app.tariff_aud(id, rev, revtype, name, start_date, end_date, description, rate, version)
values ('a32b8ba4-2dce-437c-ae94-34563b7ada43', 3, 1, 'tariff_1', '2023-11-21T12:40:40.0Z', '2023-11-22T12:40:40.0Z', 'description', 123.321, 2);

insert into tariffs_app.tariff_aud(id, rev, revtype, name, start_date, end_date, description, rate, version)
values ('5d033d70-712e-4bb9-abae-ba63f87a0b98', 2, 0, 'tariff_2', '2023-10-21T12:40:40.0Z', '2023-10-22T12:40:40.0Z', 'description_2', 456.654, 1);