package ru.neoflex.tariffs.controllers.impls;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import ru.neoflex.tariffs.controllers.TariffsController;
import ru.neoflex.tariffs.models.requests.TariffRequest;
import ru.neoflex.tariffs.models.responses.TariffResponse;
import ru.neoflex.tariffs.services.TariffsService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TariffsControllerImpl implements TariffsController {

    private final TariffsService service;

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
