package ru.neoflex.auth.services.security.impls;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.neoflex.auth.models.entity.User;
import ru.neoflex.auth.models.requests.SigninRequest;
import ru.neoflex.auth.models.requests.SignupRequest;
import ru.neoflex.auth.models.responses.JwtAuthenticationResponse;
import ru.neoflex.auth.models.responses.utils.TokenStatus;
import ru.neoflex.auth.models.responses.TokenStatusResponse;
import ru.neoflex.auth.services.security.AuthenticationService;
import ru.neoflex.auth.services.security.CustomUserDetailsService;
import ru.neoflex.auth.services.security.JwtService;
import ru.neoflex.auth.services.UserService;

import java.util.List;


@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final CustomUserDetailsService userDetailsService;

    private final UserService userService;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    @Value("${app.acceptable-remote-address-list}")
    List<String> remoteAddressList;

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
    public TokenStatusResponse getTokenStatus(HttpServletRequest request) {
        String remoteAddr = request.getRemoteAddr();
        boolean isAccept = remoteAddressList.stream()
                                            .anyMatch(it -> it.equals(remoteAddr));
        if (isAccept) {
            String authHeader = request.getHeader("Authorization");
            if (StringUtils.isEmpty(authHeader) || !StringUtils.startsWith(authHeader, "Bearer ")) {
                return new TokenStatusResponse(TokenStatus.INVALID);
            }
            String jwt = authHeader.substring(7);
            String userLogin = jwtService.extractUserName(jwt);
            if (StringUtils.isNotEmpty(userLogin) && SecurityContextHolder.getContext()
                                                                          .getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.getUserDetailsService()
                                                            .loadUserByUsername(userLogin);
                if (jwtService.isTokenValid(jwt, userDetails)) {
                    return new TokenStatusResponse(TokenStatus.VALID);
                }
            }
        }
        return new TokenStatusResponse(TokenStatus.INVALID);
    }
}