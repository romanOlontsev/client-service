package ru.neoflex.tariffs.webclients;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import ru.neoflex.tariffs.models.responses.TokenStatusResponse;

@FeignClient(value = "auth", url = "${auth.client.base-url}")
public interface AuthClient {

    @GetMapping(value = "/token/status", produces = MediaType.APPLICATION_JSON_VALUE)
    TokenStatusResponse getTokenStatus(HttpServletRequest request);
}
