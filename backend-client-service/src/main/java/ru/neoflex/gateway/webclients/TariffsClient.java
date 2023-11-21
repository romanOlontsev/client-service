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

    @GetMapping(value = "/{id}/versions/current", produces = MediaType.APPLICATION_JSON_VALUE)
    TariffResponse getCurrentVersionOfTariffById(@PathVariable(value = "id") String id);

    @GetMapping(value = "{id}/versions/{version}", produces = MediaType.APPLICATION_JSON_VALUE)
    TariffResponse getTariffByIdAndVersion(@PathVariable(value = "id") String id,
                                           @PathVariable(value = "version") Long version);

    @GetMapping(value = "/{id}/versions/previous", produces = MediaType.APPLICATION_JSON_VALUE)
    List<TariffResponse> getPreviousVersionsOfTariffById(@PathVariable(value = "id") String id);

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    void createTariff(@RequestBody TariffRequest request);

    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    void updateTariff(@PathVariable(value = "id") String id,
                      @RequestBody TariffRequest request);

    @DeleteMapping("/{id}")
    void deleteTariff(@PathVariable(value = "id") String id);
}