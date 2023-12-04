package ru.neoflex.gateway.webclients;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import ru.neoflex.gateway.models.requests.SigninRequest;
import ru.neoflex.gateway.models.requests.SignupRequest;
import ru.neoflex.gateway.models.responses.JwtAuthenticationResponse;
import ru.neoflex.gateway.models.responses.TokenStatusResponse;

@FeignClient(value = "auth", url = "${auth.client.base-url}", configuration = CustomErrorDecoder.class)
public interface AuthClient {

    @PostMapping("/api/auth/signup")
    JwtAuthenticationResponse signup(@RequestBody SignupRequest request);

    @PostMapping("/api/auth/signin")
    JwtAuthenticationResponse signin(@RequestBody SigninRequest request);

    @GetMapping(value = "/api/auth/token/status", produces = MediaType.APPLICATION_JSON_VALUE)
    TokenStatusResponse checkTokenForValidity(@RequestHeader("Token") String token,
                                              @RequestHeader("Service-Address") String address);
}
