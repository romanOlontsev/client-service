package ru.neoflex.gateway.services;

import jakarta.servlet.http.HttpServletRequest;
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

    public TariffResponse getCurrentVersionOfTariffById(HttpServletRequest httpRequest, String id) {
        String token = getAuthorizationHeaderFromRequest(httpRequest);
        TariffResponse currentVersionOfTariff = tariffsClient.getCurrentVersionOfTariffById(token, id);
        log.info("Current version of tariff: {} has been received", currentVersionOfTariff);
        return currentVersionOfTariff;
    }

    public TariffResponse getTariffByIdAndVersion(HttpServletRequest httpRequest, String id, Long version) {
        String token = getAuthorizationHeaderFromRequest(httpRequest);
        TariffResponse tariffByIdAndVersion = tariffsClient.getTariffByIdAndVersion(token, id, version);
        log.info("Version of tariff: {} has been received", tariffByIdAndVersion);
        return tariffByIdAndVersion;
    }

    public List<TariffResponse> getPreviousVersionsOfTariffById(HttpServletRequest httpRequest, String id) {
        String token = getAuthorizationHeaderFromRequest(httpRequest);
        List<TariffResponse> previousVersionsOfTariff = tariffsClient.getPreviousVersionsOfTariffById(token, id);
        log.info("Previous versions of tariff: {} has been received", previousVersionsOfTariff);
        return previousVersionsOfTariff;
    }

    public void createTariff(HttpServletRequest httpRequest, TariffRequest request) {
        String token = getAuthorizationHeaderFromRequest(httpRequest);
        log.info("Request to save: {}", request);
        tariffsClient.createTariff(token, request);
    }

    public void updateTariff(HttpServletRequest httpRequest, String id, TariffRequest request) {
        String token = getAuthorizationHeaderFromRequest(httpRequest);
        log.info("Request to update: {} for tariff with id={}", request, id);
        tariffsClient.updateTariff(token, id, request);
    }

    public void deleteTariffById(HttpServletRequest httpRequest, String id) {
        String token = getAuthorizationHeaderFromRequest(httpRequest);
        log.info("Request to delete for tariff with id={}", id);
        tariffsClient.deleteTariff(token, id);
    }

    private String getAuthorizationHeaderFromRequest(HttpServletRequest httpRequest) {
        return httpRequest.getHeader("Authorization");
    }
}
