package ru.neoflex.gateway.controllers.impls;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import ru.neoflex.gateway.controllers.TariffGatewayController;
import ru.neoflex.gateway.models.requests.TariffRequest;
import ru.neoflex.gateway.models.responses.TariffResponse;
import ru.neoflex.gateway.services.TariffGatewayService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TariffGatewayControllerImpl implements TariffGatewayController {

    private final TariffGatewayService service;

    @Override
    public TariffResponse getCurrentVersionOfTariffById(HttpServletRequest httpRequest, String id) {
        return service.getCurrentVersionOfTariffById(httpRequest, id);
    }

    @Override
    public TariffResponse getTariffByIdAndVersion(HttpServletRequest httpRequest, String id, Long version) {
        return service.getTariffByIdAndVersion(httpRequest, id, version);
    }

    @Override
    public List<TariffResponse> getPreviousVersionsOfTariffById(HttpServletRequest httpRequest, String id) {
        return service.getPreviousVersionsOfTariffById(httpRequest, id);
    }

    @Override
    public void createTariff(HttpServletRequest httpRequest, TariffRequest request) {
        service.createTariff(httpRequest, request);
    }

    @Override
    public void updateTariff(HttpServletRequest httpRequest, String id, TariffRequest request) {
        service.updateTariff(httpRequest, id, request);
    }

    @Override
    public void deleteTariff(HttpServletRequest httpRequest, String id) {
        service.deleteTariffById(httpRequest, id);
    }
}
