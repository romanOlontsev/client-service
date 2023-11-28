package ru.neoflex.gateway.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.neoflex.gateway.models.requests.SigninRequest;
import ru.neoflex.gateway.models.requests.SignupRequest;
import ru.neoflex.gateway.models.responses.JwtAuthenticationResponse;
import ru.neoflex.gateway.webclients.AuthClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthGatewayService {

    private final AuthClient client;


    public JwtAuthenticationResponse signup(SignupRequest request) {
        JwtAuthenticationResponse signup = client.signup(request);
        log.info("User registration with: {}", request);
        return signup;
    }

    public JwtAuthenticationResponse signin(SigninRequest request) {
        JwtAuthenticationResponse signin = client.signin(request);
        log.info("Login initiated by request: {}", request);
        return signin;
    }
}
