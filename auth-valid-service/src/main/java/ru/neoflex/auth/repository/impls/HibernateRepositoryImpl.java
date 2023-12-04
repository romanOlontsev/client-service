package ru.neoflex.auth.repository.impls;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import ru.neoflex.auth.repository.HibernateRepository;

@Repository
public class HibernateRepositoryImpl<T> implements HibernateRepository<T> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public <S extends T> S persist(S entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public <S extends T> S merge(S entity) {
        entityManager.merge(entity);
        return entity;
    }
}