package ru.neoflex.tariffs.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.neoflex.tariffs.configuration.TopicNames;
import ru.neoflex.tariffs.exceptions.BadRequestException;
import ru.neoflex.tariffs.exceptions.DataNotFoundException;
import ru.neoflex.tariffs.mappers.TariffMapper;
import ru.neoflex.tariffs.models.entities.Tariff;
import ru.neoflex.tariffs.models.requests.TariffRequest;
import ru.neoflex.tariffs.models.responses.ProductResponse;
import ru.neoflex.tariffs.models.responses.TariffMessage;
import ru.neoflex.tariffs.models.responses.TariffResponse;
import ru.neoflex.tariffs.repositories.TariffRepository;
import ru.neoflex.tariffs.webclients.ProductClient;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TariffsService {

    private final ProductClient productClient;

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

    @Transactional
    public TariffResponse getTariffByIdAndVersion(String id, Long version) {
        Tariff foundTariff = repository.findTariffByIdAndVersion(UUID.fromString(id), version)
                                       .orElseThrow(() -> new DataNotFoundException(
                                               String.format("Tariff with id=%s and version %s not found", id, version)));
        return mapper.tariffResponseFromTariff(foundTariff);
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
        Tariff savedTariff = repository.save(foundTariff);
        kafkaService.sendMessage(TopicNames.TARIFF_UPDATED.name(),
                                 TariffMessage.builder()
                                              .tariffId(savedTariff.getId())
                                              .tariffVersion(savedTariff.getVersion() + 1)
                                              .build());
    }

    public void deleteTariffById(String id) {
        List<ProductResponse> foundProductList = productClient.getProductsByTariffId(id);
        if (!foundProductList.isEmpty()) {
            throw new BadRequestException(String.format("There are products that refer to a tariff with an id=%s", id));
        }

        Tariff foundTariff = findTariffById(id);
        repository.delete(foundTariff);
        log.info("The tariff with id={} has been deleted", id);
    }

    private Tariff findTariffById(String id) {
        return repository.findTariffById(UUID.fromString(id))
                         .orElseThrow(() -> new DataNotFoundException(
                                 String.format("Tariff with id=%s not found", id)));
    }
}
