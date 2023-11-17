package ru.neoflex.gateway.controllers.impls;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import ru.neoflex.gateway.controllers.TariffGatewayController;
import ru.neoflex.gateway.models.requests.TariffRequest;
import ru.neoflex.gateway.services.GatewayService;

@RestController
@RequiredArgsConstructor
public class TariffGatewayControllerImpl implements TariffGatewayController {

    private final GatewayService service;

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
