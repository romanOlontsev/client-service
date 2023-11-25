package ru.neoflex.auth.controllers.impls;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.neoflex.auth.controllers.AuthController;
import ru.neoflex.auth.models.requests.SigninRequest;
import ru.neoflex.auth.models.requests.SignupRequest;
import ru.neoflex.auth.models.responses.JwtAuthenticationResponse;
import ru.neoflex.auth.models.responses.UserDetailsResponse;
import ru.neoflex.auth.services.security.AuthenticationService;

@RestController
@RequiredArgsConstructor
public class AuthControllerImpl implements AuthController {

    private final AuthenticationService authenticationService;

    @Override
    public JwtAuthenticationResponse signup(@RequestBody SignupRequest request) {
        return authenticationService.signup(request);
    }

    @Override
    public JwtAuthenticationResponse signin(@RequestBody SigninRequest request) {
        return authenticationService.signin(request);
    }

    @Override
    public UserDetailsResponse getUserDetails(String token) {
        return authenticationService.getUserDetailsIfValidToken(token);
    }
}