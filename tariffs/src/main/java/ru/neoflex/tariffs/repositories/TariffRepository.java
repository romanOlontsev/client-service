package ru.neoflex.tariffs.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.neoflex.tariffs.models.entities.Tariff;

import java.util.Optional;
import java.util.UUID;

public interface TariffRepository extends JpaRepository<Tariff, UUID>, TariffAuditRepository {

    @Query("SELECT t FROM Tariff t WHERE t.id=:id")
    Optional<Tariff> findTariffById(@Param("id") UUID id);
}
