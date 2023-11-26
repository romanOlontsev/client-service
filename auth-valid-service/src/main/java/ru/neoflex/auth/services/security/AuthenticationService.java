package ru.neoflex.auth.services.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.userdetails.UserDetails;
import ru.neoflex.auth.models.requests.SigninRequest;
import ru.neoflex.auth.models.requests.SignupRequest;
import ru.neoflex.auth.models.responses.JwtAuthenticationResponse;
import ru.neoflex.auth.models.responses.TokenStatusResponse;
import ru.neoflex.auth.models.responses.UserDetailsResponse;

public interface AuthenticationService {
    JwtAuthenticationResponse signup(SignupRequest request);

    JwtAuthenticationResponse signin(SigninRequest request);

    TokenStatusResponse getUserDetailsIfValidToken(String token);
}