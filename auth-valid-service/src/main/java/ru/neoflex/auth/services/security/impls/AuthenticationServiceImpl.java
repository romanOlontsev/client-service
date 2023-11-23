package ru.neoflex.auth.services.security.impls;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import ru.neoflex.auth.models.entity.User;
import ru.neoflex.auth.models.requests.SigninRequest;
import ru.neoflex.auth.models.requests.SignupRequest;
import ru.neoflex.auth.models.responses.JwtAuthenticationResponse;
import ru.neoflex.auth.services.security.AuthenticationService;
import ru.neoflex.auth.services.security.JwtService;
import ru.neoflex.auth.services.UserService;


@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserService userService;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    @Override
    public JwtAuthenticationResponse signup(SignupRequest request) {
        User savedUser = userService.addUser(request);
        String jwt = jwtService.generateToken(savedUser);
        return JwtAuthenticationResponse.builder()
                                        .id(savedUser.getId())
                                        .token(jwt)
                                        .build();
    }

    @Override
    public JwtAuthenticationResponse signin(SigninRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getLogin(), request.getPassword()));
        User userByLogin = userService.getUserByLogin(request.getLogin());
        String jwt = jwtService.generateToken(userByLogin);
        return JwtAuthenticationResponse.builder()
                                        .token(jwt)
                                        .id(userByLogin.getId())
                                        .build();
    }
}