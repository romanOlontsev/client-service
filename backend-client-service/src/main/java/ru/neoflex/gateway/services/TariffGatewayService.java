package ru.neoflex.gateway.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.neoflex.gateway.models.requests.TariffRequest;
import ru.neoflex.gateway.models.responses.TariffResponse;
import ru.neoflex.gateway.webclients.TariffsClient;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TariffGatewayService {

    private final TariffsClient tariffsClient;

    public TariffResponse getCurrentVersionOfTariffById(String id) {
        TariffResponse currentVersionOfTariff = tariffsClient.getCurrentVersionOfTariffById(id);
        log.info("Current version of tariff: {} has been received", currentVersionOfTariff);
        return currentVersionOfTariff;
    }

    public TariffResponse getTariffByIdAndVersion(String id, Long version) {
        TariffResponse tariffByIdAndVersion = tariffsClient.getTariffByIdAndVersion(id, version);
        log.info("Version of tariff: {} has been received", tariffByIdAndVersion);
        return tariffByIdAndVersion;
    }

    public List<TariffResponse> getPreviousVersionsOfTariffById(String id) {
        List<TariffResponse> previousVersionsOfTariff = tariffsClient.getPreviousVersionsOfTariffById(id);
        log.info("Previous versions of tariff: {} has been received", previousVersionsOfTariff);
        return previousVersionsOfTariff;
    }

    public void createTariff(TariffRequest request) {
        log.info("Request to save: {}", request);
        tariffsClient.createTariff(request);
    }

    public void updateTariff(String id, TariffRequest request) {
        log.info("Request to update: {} for tariff with id={}", request, id);
        tariffsClient.updateTariff(id, request);
    }

    public void deleteTariffById(String id) {
        log.info("Request to delete for tariff with id={}", id);
        tariffsClient.deleteTariff(id);
    }
}
