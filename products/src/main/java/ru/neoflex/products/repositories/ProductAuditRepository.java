package ru.neoflex.products.repositories;

import ru.neoflex.products.models.entities.Product;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductAuditRepository {

    List<Product> findPreviousVersionsOfProductById(UUID id);

    Optional<Product> findPreviousVersionOfProductById(UUID id);

    Optional<Product> findVersionOfProductForCertainPeriodById(UUID id, LocalDateTime dateTime);
}
