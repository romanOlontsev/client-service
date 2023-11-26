package ru.neoflex.tariffs.webclients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.neoflex.tariffs.models.responses.TokenStatusResponse;
import ru.neoflex.tariffs.models.responses.UserDetailsResponse;

@FeignClient(value = "auth", url = "${auth.client.base-url}")
public interface AuthClient {

    @GetMapping(value = "/api/auth/token/status", produces = MediaType.APPLICATION_JSON_VALUE)
    TokenStatusResponse checkTokenForValidity(@RequestParam("token") String token);
}
