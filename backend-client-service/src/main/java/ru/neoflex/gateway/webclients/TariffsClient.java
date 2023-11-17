package ru.neoflex.gateway.webclients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.neoflex.gateway.configuration.CustomFeignClientConfiguration;
import ru.neoflex.gateway.models.requests.TariffRequest;

@FeignClient(value = "tariffs", url = "${tariffs.client.base-url}",
        configuration = {CustomErrorDecoder.class, CustomFeignClientConfiguration.class})
public interface TariffsClient {

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    void createTariff(@RequestBody TariffRequest request);

    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    void updateTariff(@PathVariable(value = "id") String id,
                      @RequestBody TariffRequest request);

    @DeleteMapping("/{id}")
    void deleteTariff(@PathVariable(value = "id") String id);
}