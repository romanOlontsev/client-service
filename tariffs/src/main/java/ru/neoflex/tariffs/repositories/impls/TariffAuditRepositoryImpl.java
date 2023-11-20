package ru.neoflex.tariffs.repositories.impls;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import ru.neoflex.tariffs.models.entities.Tariff;
import ru.neoflex.tariffs.repositories.TariffAuditRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class TariffAuditRepositoryImpl implements TariffAuditRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Tariff> findPreviousVersionsOfTariffById(UUID id) {
        List<?> resultList = AuditReaderFactory.get(entityManager)
                                               .createQuery()
                                               .forRevisionsOfEntity(Tariff.class, true, true)
                                               .add(AuditEntity.id()
                                                               .eq(id))
                                               .addOrder(AuditEntity.revisionNumber()
                                                                    .desc())
                                               .getResultList();
        return resultList.stream()
                         .filter(Tariff.class::isInstance)
                         .map(Tariff.class::cast)
                         .skip(1)
                         .collect(Collectors.toList());
    }

    @Override
    public Optional<Tariff> findTariffByIdAndVersion(UUID id, Long version) {
        Tariff singleResult;
        try {
            singleResult = (Tariff) AuditReaderFactory.get(entityManager)
                                                      .createQuery()
                                                      .forRevisionsOfEntity(Tariff.class, true, true)
                                                      .add(AuditEntity.id()
                                                                      .eq(id))
                                                      .add(AuditEntity.property("version")
                                                                      .eq(version))
                                                      .setMaxResults(1)
                                                      .getSingleResult();
        } catch (NoResultException e) {
            return Optional.empty();
        }
        return Optional.of(singleResult);
    }
}
