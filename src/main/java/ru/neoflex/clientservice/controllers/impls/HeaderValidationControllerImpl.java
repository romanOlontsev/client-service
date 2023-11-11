package ru.neoflex.clientservice.controllers.impls;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import ru.neoflex.clientservice.controllers.HeaderValidationController;
import ru.neoflex.clientservice.models.requests.HeaderValidationRequest;
import ru.neoflex.clientservice.models.responses.HeaderValidationResponse;
import ru.neoflex.clientservice.services.HeaderValidationService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class HeaderValidationControllerImpl implements HeaderValidationController {

    private final HeaderValidationService service;

    @Override
    public List<HeaderValidationResponse> getHeaderValidationList() {
        return service.getHeaderValidationRules();
    }

    @Override
    public void addHeaderValidation(HeaderValidationRequest request) {
        service.addHeaderValidation(request);
    }

    @Override
    public void updateHeaderValidation(HeaderValidationRequest request) {
        service.updateHeaderValidation(request);
    }

    @Override
    public void deleteHeaderValidation(String header) {
        service.deleteHeaderValidation(header);
    }
}
