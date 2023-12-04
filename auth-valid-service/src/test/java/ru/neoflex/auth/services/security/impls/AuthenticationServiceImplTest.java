package ru.neoflex.auth.services.security.impls;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.ObservationAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.neoflex.auth.exceptions.UnregisteredAddressException;
import ru.neoflex.auth.models.entity.User;
import ru.neoflex.auth.models.requests.SigninRequest;
import ru.neoflex.auth.models.requests.SignupRequest;
import ru.neoflex.auth.models.responses.JwtAuthenticationResponse;
import ru.neoflex.auth.models.responses.TokenStatusResponse;
import ru.neoflex.auth.models.responses.utils.TokenStatus;
import ru.neoflex.auth.services.UserService;
import ru.neoflex.auth.services.ValidAddressService;
import ru.neoflex.auth.services.security.JwtService;

import java.util.Collections;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTest {

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @Mock
    private UserService userService;

    @Mock
    private JwtService jwtService;

    @Mock
    private ObservationAuthenticationManager manager;

    @Mock
    private ValidAddressService addressService;

    @Test
    void signup_shouldReturnJwtAuthenticationResponse() {
        SignupRequest request = SignupRequest.builder()
                                             .login("test")
                                             .password("test")
                                             .build();
        User user = User.builder()
                        .id(-1L)
                        .login(request.getLogin())
                        .password(request.getPassword())
                        .build();
        String jwtToken = "expectedJwtToken";

        when(userService.addUser(any())).thenReturn(user);
        when(jwtService.generateToken(any())).thenReturn(jwtToken);

        JwtAuthenticationResponse response = authenticationService.signup(request);
        assertAll(
                () -> assertThat(response).isNotNull()
                                          .extracting(JwtAuthenticationResponse::getId)
                                          .isEqualTo(user.getId()),
                () -> assertThat(response).isNotNull()
                                          .extracting(JwtAuthenticationResponse::getToken)
                                          .isEqualTo(jwtToken)
                 );
    }

    @Test
    void signin_shouldReturnJwtAuthenticationResponse() {
        SigninRequest request = SigninRequest.builder()
                                             .login("test")
                                             .password("test")
                                             .build();
        User user = User.builder()
                        .id(-1L)
                        .login(request.getLogin())
                        .password(request.getPassword())
                        .build();
        String jwtToken = "expectedJwtToken";

        when(manager.authenticate(any())).thenReturn(Mockito.mock(Authentication.class));
        when(userService.getUserByLogin(any())).thenReturn(user);
        when(jwtService.generateToken(any())).thenReturn(jwtToken);

        JwtAuthenticationResponse response = authenticationService.signin(request);
        assertAll(
                () -> assertThat(response).isNotNull()
                                          .extracting(JwtAuthenticationResponse::getId)
                                          .isEqualTo(user.getId()),
                () -> assertThat(response).isNotNull()
                                          .extracting(JwtAuthenticationResponse::getToken)
                                          .isEqualTo(jwtToken)
                 );
    }

    @Test
    void checkTokenForValidity_shouldReturnTokenStatusResponse_valid() {
        String jwtToken = "JwtToken";
        String address = "http://some.con";
        Set<String> addressSet = Set.of(address);

        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(addressService.getValidAddressList()).thenReturn(addressSet);
        when(jwtService.extractUserName(any())).thenReturn("test");
        when(jwtService.isTokenExpired(any())).thenReturn(false);

        TokenStatusResponse response = authenticationService.checkTokenForValidity(jwtToken, address);
        assertAll(
                () -> assertThat(response).isNotNull()
                                          .extracting(TokenStatusResponse::getTokenStatus)
                                          .isEqualTo(TokenStatus.VALID.name())
                 );
    }

    @Test
    void checkTokenForValidity_shouldReturnTokenStatusResponse_invalid_emptyLogin() {
        String jwtToken = "JwtToken";
        String address = "http://some.con";
        Set<String> addressSet = Set.of(address);

        when(addressService.getValidAddressList()).thenReturn(addressSet);
        when(jwtService.extractUserName(any())).thenReturn("");

        TokenStatusResponse response = authenticationService.checkTokenForValidity(jwtToken, address);
        assertAll(
                () -> assertThat(response).isNotNull()
                                          .extracting(TokenStatusResponse::getTokenStatus)
                                          .isEqualTo(TokenStatus.INVALID.name())
                 );
    }

    @Test
    void checkTokenForValidity_shouldReturnTokenStatusResponse_invalid_authIsNull() {
        String jwtToken = "JwtToken";
        String address = "http://some.con";
        Set<String> addressSet = Set.of(address);

        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(null);
        SecurityContextHolder.setContext(securityContext);
        when(addressService.getValidAddressList()).thenReturn(addressSet);
        when(jwtService.extractUserName(any())).thenReturn("test");
        SecurityContextHolder.getContext()
                             .setAuthentication(null);

        TokenStatusResponse response = authenticationService.checkTokenForValidity(jwtToken, address);
        assertAll(
                () -> assertThat(response).isNotNull()
                                          .extracting(TokenStatusResponse::getTokenStatus)
                                          .isEqualTo(TokenStatus.INVALID.name())
                 );
    }

    @Test
    void checkTokenForValidity_shouldReturnTokenStatusResponse_invalid_tokenExpired() {
        String jwtToken = "JwtToken";
        String address = "http://some.con";
        Set<String> addressSet = Set.of(address);

        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(addressService.getValidAddressList()).thenReturn(addressSet);
        when(jwtService.extractUserName(any())).thenReturn("test");
        when(jwtService.isTokenExpired(any())).thenReturn(true);

        TokenStatusResponse response = authenticationService.checkTokenForValidity(jwtToken, address);
        assertAll(
                () -> assertThat(response).isNotNull()
                                          .extracting(TokenStatusResponse::getTokenStatus)
                                          .isEqualTo(TokenStatus.INVALID.name())
                 );
    }

    @Test
    void checkTokenForValidity_shouldThrowUnregisteredAddressException() {
        String jwtToken = "JwtToken";
        String address = "http://some.con";

        when(addressService.getValidAddressList()).thenReturn(Collections.emptySet());

        assertAll(
                () -> assertThatThrownBy(
                        () -> authenticationService.checkTokenForValidity(jwtToken, address))
                        .isInstanceOf(UnregisteredAddressException.class)
                        .hasMessage("Request address not registered")
                 );
    }
}