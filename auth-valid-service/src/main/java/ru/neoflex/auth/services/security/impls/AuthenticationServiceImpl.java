package ru.neoflex.auth.services.security.impls;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.neoflex.auth.exceptions.UnregisteredAddressException;
import ru.neoflex.auth.models.entity.User;
import ru.neoflex.auth.models.requests.SigninRequest;
import ru.neoflex.auth.models.requests.SignupRequest;
import ru.neoflex.auth.models.responses.JwtAuthenticationResponse;
import ru.neoflex.auth.models.responses.TokenStatusResponse;
import ru.neoflex.auth.models.responses.utils.TokenStatus;
import ru.neoflex.auth.services.UserService;
import ru.neoflex.auth.services.ValidAddressService;
import ru.neoflex.auth.services.security.AuthenticationService;
import ru.neoflex.auth.services.security.JwtService;

import java.util.Set;


@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserService userService;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    private final ValidAddressService addressService;

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

    @Override
    public TokenStatusResponse checkTokenForValidity(String token, String address) {
        checkRequestAddress(address);
        String userLogin = jwtService.extractUserName(token);
        if (StringUtils.isNotEmpty(userLogin) && SecurityContextHolder.getContext()
                                                                      .getAuthentication() != null) {
            if (!jwtService.isTokenExpired(token)) {
                return new TokenStatusResponse(TokenStatus.VALID);
            }
        }
        return new TokenStatusResponse(TokenStatus.INVALID);
    }

    private void checkRequestAddress(String address) {
        Set<String> validAddressList = addressService.getValidAddressList();
        if (!validAddressList.contains(address)) {
            throw new UnregisteredAddressException("Request address not registered");
        }
    }
}
