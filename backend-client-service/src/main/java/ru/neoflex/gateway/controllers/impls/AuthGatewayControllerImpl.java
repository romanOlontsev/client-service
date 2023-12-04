package ru.neoflex.gateway.controllers.impls;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.neoflex.gateway.controllers.AuthGatewayController;
import ru.neoflex.gateway.models.requests.SigninRequest;
import ru.neoflex.gateway.models.requests.SignupRequest;
import ru.neoflex.gateway.models.responses.JwtAuthenticationResponse;
import ru.neoflex.gateway.services.AuthGatewayService;

@RestController
@RequiredArgsConstructor
public  class AuthGatewayControllerImpl implements AuthGatewayController {

    private final AuthGatewayService service;

    @Override
    public JwtAuthenticationResponse signup(@RequestBody SignupRequest request) {
        return service.signup(request);
    }

    @Override
    public JwtAuthenticationResponse signin(@RequestBody SigninRequest request) {
        return service.signin(request);
    }
}
