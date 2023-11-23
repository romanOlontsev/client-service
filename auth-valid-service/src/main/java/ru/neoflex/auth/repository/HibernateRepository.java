package ru.neoflex.auth.repository;

public interface HibernateRepository<T> {

    <S extends T> S persist(S entity);

    <S extends T> S merge(S entity);
}
