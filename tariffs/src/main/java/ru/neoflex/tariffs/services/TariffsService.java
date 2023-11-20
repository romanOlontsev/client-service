package ru.neoflex.tariffs.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.neoflex.tariffs.exceptions.DataNotFoundException;
import ru.neoflex.tariffs.mappers.TariffMapper;
import ru.neoflex.tariffs.models.entities.Tariff;
import ru.neoflex.tariffs.models.requests.TariffRequest;
import ru.neoflex.tariffs.models.responses.TariffResponse;
import ru.neoflex.tariffs.repositories.TariffRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TariffsService {

    private final KafkaService kafkaService;

    private final TariffRepository repository;

    private final TariffMapper mapper;

    public TariffResponse getCurrentVersionOfTariffById(String id) {
        Tariff foundTariff = findTariffById(id);
        TariffResponse response = mapper.tariffResponseFromTariff(foundTariff);
        log.info("The current version of the tariff: {} has been received", response);
        return response;
    }

    @Transactional
    public List<TariffResponse> getPreviousVersionsOfTariffById(String id) {
        List<Tariff> all = repository.findPreviousVersionsOfTariffById(UUID.fromString(id));
        List<TariffResponse> response = mapper.tariffResponseListFromTariffList(all);
        log.info("Previous versions of the tariff: {} were received", response);
        return response;
    }

    public void createTariff(TariffRequest request) {
        Tariff tariff = mapper.tariffFromTariffRequest(request);
        Tariff savedTariff = repository.save(tariff);
        log.info("The tariff: {} has been saved", savedTariff);
    }

    @Transactional
    public void updateTariff(String id, TariffRequest request) {
        Tariff foundTariff = findTariffById(id);
        mapper.updateTariffFromTariffRequest(foundTariff, request);
        log.info("The tariff: {} has been updated", foundTariff);
        kafkaService.sendMessage(id);
    }

    public void deleteTariffById(String id) {
        repository.deleteById(UUID.fromString(id));
        log.info("The tariff with id={} has been deleted", id);
    }

    private Tariff findTariffById(String id) {
        return repository.findTariffById(UUID.fromString(id))
                         .orElseThrow(() -> new DataNotFoundException(
                                 String.format("Tariff with id=%s not found", id)));
    }
}
