package ru.neoflex.products.repositories.impls;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import ru.neoflex.products.models.entities.Product;
import ru.neoflex.products.repositories.ProductAuditRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class ProductAuditRepositoryImpl implements ProductAuditRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Product> findPreviousVersionsOfProductById(UUID id) {
        List<?> resultList = AuditReaderFactory.get(entityManager)
                                               .createQuery()
                                               .forRevisionsOfEntity(Product.class, true, true)
                                               .add(AuditEntity.id()
                                                               .eq(id))
                                               .addOrder(AuditEntity.revisionNumber()
                                                                    .desc())
                                               .getResultList();
        return resultList.stream()
                         .filter(Product.class::isInstance)
                         .map(Product.class::cast)
                         .skip(1)
                         .collect(Collectors.toList());
    }

    @Override
    public Optional<Product> findPreviousVersionOfProductById(UUID id) {
        AuditReader reader = AuditReaderFactory.get(entityManager);
        List<Number> revNumbers = reader.getRevisions(Product.class, id);
        if (revNumbers.size() < 2) {
            return Optional.empty();
        }
        Product productPrevious = reader.find(Product.class, id, revNumbers.get(revNumbers.size() - 2));
        return Optional.of(productPrevious);
    }

    @Override
    public Optional<Product> findVersionOfProductForCertainPeriodById(UUID id, LocalDateTime dateTime) {
        Product singleResult;
        try {
            singleResult = (Product) AuditReaderFactory.get(entityManager)
                                                       .createQuery()
                                                       .forRevisionsOfEntity(Product.class, true, true)
                                                       .add(AuditEntity.id()
                                                                       .eq(id))
                                                       .addOrder(AuditEntity.revisionNumber()
                                                                            .desc())
                                                       .add(AuditEntity.revisionProperty("timestamp")
                                                                       .lt(dateTime))
                                                       .setMaxResults(1)
                                                       .getSingleResult();
        } catch (NoResultException e) {
            return Optional.empty();
        }
        return Optional.of(singleResult);
    }
}
