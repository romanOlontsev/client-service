package ru.neoflex.users.controllers.impls;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import ru.neoflex.users.controllers.HeaderValidationController;
import ru.neoflex.users.models.requests.HeaderValidationRequest;
import ru.neoflex.users.models.responses.HeaderValidationResponse;
import ru.neoflex.users.services.HeaderValidationService;

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
