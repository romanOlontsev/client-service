package ru.neoflex.gateway.controllers.impls;

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
    public TariffResponse getCurrentVersionOfTariffById(String id) {
        return service.getCurrentVersionOfTariffById(id);
    }

    @Override
    public TariffResponse getTariffByIdAndVersion(String id, Long version) {
        return service.getTariffByIdAndVersion(id, version);
    }

    @Override
    public List<TariffResponse> getPreviousVersionsOfTariffById(String id) {
        return service.getPreviousVersionsOfTariffById(id);
    }

    @Override
    public void createTariff(TariffRequest request) {
        service.createTariff(request);
    }

    @Override
    public void updateTariff(String id, TariffRequest request) {
        service.updateTariff(id, request);
    }

    @Override
    public void deleteTariff(String id) {
        service.deleteTariffById(id);
    }
}
