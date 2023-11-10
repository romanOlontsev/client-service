package ru.neoflex.clientservice.controllers.impls;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.neoflex.clientservice.models.requests.HeaderValidationRequest;
import ru.neoflex.clientservice.services.HeaderValidationService;

@RestController
@RequiredArgsConstructor
public class HeaderValidationController {

    private final HeaderValidationService service;

    @PostMapping("/admin/validation")
    public void addHeaderValidation(HeaderValidationRequest request) {
        service.addHeaderValidation(request);
    }
}
