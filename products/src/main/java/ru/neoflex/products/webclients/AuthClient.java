package ru.neoflex.products.webclients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import ru.neoflex.products.models.responses.TokenStatusResponse;

@FeignClient(value = "auth", url = "${auth.client.base-url}", configuration = CustomErrorDecoder.class)
public interface AuthClient {

    @GetMapping(value = "/api/auth/token/status", produces = MediaType.APPLICATION_JSON_VALUE)
    TokenStatusResponse checkTokenForValidity(@RequestHeader("Token") String token,
                                              @RequestHeader("Service-Address") String address);
}
