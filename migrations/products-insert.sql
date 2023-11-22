--liquibase formatted sql

--changeset admin:5
insert into products_app.product(id, name, type, start_date, end_date, description, tariff, author, product_version, tariff_version)
values ('6fb8f572-e70d-4416-ac09-214174d04b52', 'product_1', 'LOAN', '2023-11-21T12:40:40.0Z', '2023-11-22T12:40:40.0Z', 'description', 'a32b8ba4-2dce-437c-ae94-34563b7ada43', null, 2, 2);
insert into products_app.product(id, name, type, start_date, end_date, description, tariff, author, product_version, tariff_version)
values ('50f11359-b8a9-460d-b538-f8dcf04c2085', 'product_2', 'CARD', '2023-10-21T12:40:40.0Z', '2023-10-22T12:40:40.0Z', 'description_2', '5d033d70-712e-4bb9-abae-ba63f87a0b98', null,  1, 1);

insert into products_app.revinfo(revtstmp) values ('2023-10-21T12:40:40.0Z');
insert into products_app.revinfo(revtstmp) values ('2023-11-21T12:42:42.0Z');
insert into products_app.revinfo(revtstmp) values ('2023-11-21T12:45:45.0Z');


insert into products_app.product_aud(id, rev, revtype, name, type, start_date, end_date, description, tariff, author, product_version, tariff_version)
values ('6fb8f572-e70d-4416-ac09-214174d04b52', 1, 0, 'product_1', 'LOAN', '2023-11-21T12:40:40.0Z', '2023-11-22T12:40:40.0Z', 'description', 'a32b8ba4-2dce-437c-ae94-34563b7ada43', null, 1, 1);
insert into products_app.product_aud(id, rev, revtype, name, type, start_date, end_date, description, tariff, author, product_version, tariff_version)
values ('6fb8f572-e70d-4416-ac09-214174d04b52', 3, 1, 'product_1', 'LOAN', '2023-11-21T12:40:40.0Z', '2023-11-22T12:40:40.0Z', 'description', 'a32b8ba4-2dce-437c-ae94-34563b7ada43', null, 2, 2);

insert into products_app.product_aud(id, rev, revtype, name, type, start_date, end_date, description, tariff, author, product_version, tariff_version)
values ('50f11359-b8a9-460d-b538-f8dcf04c2085', 2, 0, 'product_2', 'CARD', '2023-10-21T12:40:40.0Z', '2023-10-22T12:40:40.0Z', 'description_2', '5d033d70-712e-4bb9-abae-ba63f87a0b98', null, 1, 1);