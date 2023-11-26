package ru.neoflex.gateway.webclients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.neoflex.gateway.configuration.CustomFeignClientConfiguration;
import ru.neoflex.gateway.models.requests.TariffRequest;
import ru.neoflex.gateway.models.responses.TariffResponse;

import java.util.List;

@FeignClient(value = "tariffs", url = "${tariffs.client.base-url}",
        configuration = {CustomErrorDecoder.class, CustomFeignClientConfiguration.class})
public interface TariffsClient {

    @GetMapping(value = "/api/tariffs/{id}/versions/current", produces = MediaType.APPLICATION_JSON_VALUE)
    TariffResponse getCurrentVersionOfTariffById(
            @RequestHeader("Authorization") String token,
            @PathVariable(value = "id") String id);

    @GetMapping(value = "/api/tariffs/{id}/versions/{version}", produces = MediaType.APPLICATION_JSON_VALUE)
    TariffResponse getTariffByIdAndVersion(
            @RequestHeader("Authorization") String token,
            @PathVariable(value = "id") String id,
            @PathVariable(value = "version") Long version);

    @GetMapping(value = "/api/tariffs/{id}/versions/previous", produces = MediaType.APPLICATION_JSON_VALUE)
    List<TariffResponse> getPreviousVersionsOfTariffById(
            @RequestHeader("Authorization") String token,
            @PathVariable(value = "id") String id);

    @PostMapping(value = "/api/tariffs", consumes = MediaType.APPLICATION_JSON_VALUE)
    void createTariff(
            @RequestHeader("Authorization") String token,
            @RequestBody TariffRequest request);

    @PatchMapping(value = "/api/tariffs/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    void updateTariff(
            @RequestHeader("Authorization") String token,
            @PathVariable(value = "id") String id,
            @RequestBody TariffRequest request);

    @DeleteMapping("/api/tariffs/{id}")
    void deleteTariff(
            @RequestHeader("Authorization") String token,
            @PathVariable(value = "id") String id);
}