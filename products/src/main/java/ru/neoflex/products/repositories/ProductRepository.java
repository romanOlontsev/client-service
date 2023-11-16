package ru.neoflex.products.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.neoflex.products.models.entities.Product;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID>, ProductAuditRepository {

    @Query("SELECT p FROM Product p WHERE p.id=:id")
    Optional<Product> findProjectById(@Param("id") UUID id);
}
