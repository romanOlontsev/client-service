package ru.neoflex.auth.services.security;

import ru.neoflex.auth.models.requests.SigninRequest;
import ru.neoflex.auth.models.requests.SignupRequest;
import ru.neoflex.auth.models.responses.JwtAuthenticationResponse;

public interface AuthenticationService {
    JwtAuthenticationResponse signup(SignupRequest request);

    JwtAuthenticationResponse signin(SigninRequest request);
}