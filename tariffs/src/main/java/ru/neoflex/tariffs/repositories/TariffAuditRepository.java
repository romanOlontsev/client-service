package ru.neoflex.tariffs.repositories;

import ru.neoflex.tariffs.models.entities.Tariff;

import java.util.List;
import java.util.UUID;

public interface TariffAuditRepository {

    List<Tariff> findPreviousVersionsOfTariffById(UUID id);

}
