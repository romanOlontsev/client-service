package ru.neoflex.auth.services.security.impls;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.annotation.Rollback;
import ru.neoflex.auth.models.entity.User;
import ru.neoflex.auth.models.requests.SignupRequest;
import ru.neoflex.auth.models.responses.JwtAuthenticationResponse;
import ru.neoflex.auth.services.UserService;
import ru.neoflex.auth.services.ValidAddressService;
import ru.neoflex.auth.services.security.JwtService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@EnableFeignClients
@Rollback
@Transactional
class AuthenticationServiceImplTest {

    private AuthenticationServiceImpl authenticationService;

    private UserService userService;

    private static JwtService jwtService;

    private AuthenticationManager authenticationManager;

    private ValidAddressService validAddressService;

    @BeforeAll
    static void beforeAll() {
        jwtService = new JwtServiceImpl();
    }

    @Test
    void signup_shouldReturnNotNullToken() {
        SignupRequest request = SignupRequest.builder()
                                             .login("test")
                                             .password("test")
                                             .build();
        User user = User.builder()
                        .login(request.getLogin())
                        .password(request.getPassword())
                        .id(-1L)
                        .build();

        when(userService.addUser(any())).thenReturn(user);
        JwtAuthenticationResponse response = authenticationService.signup(request);

        assertAll(
                () -> assertThat(response).isNotNull()
                                          .extracting(JwtAuthenticationResponse::getId)
                                          .isEqualTo(user.getId()),
                () -> assertThat(response).extracting(JwtAuthenticationResponse::getToken)
                                          .isNotNull()
        );
    }

    @Test
    void signin() {
    }

    @Test
    void checkTokenForValidity() {
    }
}