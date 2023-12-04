package ru.neoflex.auth.services.security.impls;

import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;
import ru.neoflex.auth.models.entity.User;
import ru.neoflex.auth.services.security.JwtService;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class JwtServiceImplTest {

    private static JwtService service;

    @BeforeAll
    static void beforeAll() throws IOException {
        service = new JwtServiceImpl();
        FileReader reader = new FileReader("src/test/resources/application.properties");
        Properties prop = new Properties();
        prop.load(reader);
        String secret = prop.getProperty("token.signing.key");
        ReflectionTestUtils.setField(service, "jwtSigningKey", secret);
    }

    @Test
    void extractUserName_shouldReturnExpectedString() {
        String expected = "test";
        String generatedToken = service.generateToken(User.builder()
                                                          .login(expected)
                                                          .build());

        String response = service.extractUserName(generatedToken);

        assertAll(
                () -> assertThat(response).isNotNull()
                                          .isEqualTo(expected)
                 );
    }

    @Test
    void generateToken_shouldReturnNotNullToken() {
        UserDetails userDetails = User.builder()
                                      .login("test")
                                      .build();

        String generatedToken = service.generateToken(userDetails);

        assertAll(
                () -> assertThat(generatedToken).isNotNull()
                 );
    }

    @Test
    void isTokenValid_returnTrue() {
        UserDetails userDetails = User.builder()
                                      .login("test")
                                      .build();
        String generatedToken = service.generateToken(userDetails);
        boolean response = service.isTokenValid(generatedToken, userDetails);

        assertAll(
                () -> assertThat(generatedToken).isNotNull(),
                () -> assertThat(response).isTrue()
                 );
    }

    @Test
    void isTokenValid_returnFalse() {
        UserDetails userDetails = User.builder()
                                      .login("test")
                                      .build();
        UserDetails uncorrectuserDetails = User.builder()
                                               .login("notTest")
                                               .build();
        String generatedToken = service.generateToken(userDetails);
        boolean response = service.isTokenValid(generatedToken, uncorrectuserDetails);

        assertAll(
                () -> assertThat(generatedToken).isNotNull(),
                () -> assertThat(response).isFalse()
                 );
    }

    @Test
    void isTokenExpired_shouldTrue() {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzAxMTEzODE0LCJleHAiOjE3MDExMTM4MTR9.SeNwI5WYZ2br-5hHWMTiTYhm6wjzk-wSRb720qYprxU";

        assertAll(
                () -> assertThatThrownBy(() -> service.isTokenExpired(token))
                        .isInstanceOf(ExpiredJwtException.class)
                 );
    }
}