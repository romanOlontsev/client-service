package ru.neoflex.auth.controllers.impls;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import ru.neoflex.auth.controllers.ValidAddressController;
import ru.neoflex.auth.services.ValidAddressService;

@RestController
@RequiredArgsConstructor
public class ValidAddressControllerImpl implements ValidAddressController {

    private final ValidAddressService service;

    @Override
    public void addValidAddress(String addressName) {
        service.addAddress(addressName);
    }

    @Override
    public void deleteValidAddress(String addressName) {
        service.deleteAddress(addressName);
    }
}
