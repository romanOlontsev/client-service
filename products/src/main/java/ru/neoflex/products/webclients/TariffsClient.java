package ru.neoflex.products.webclients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.neoflex.products.models.responses.TariffResponse;

@FeignClient(value = "tariffs", url = "${tariffs.client.base-url}", configuration = CustomErrorDecoder.class)
public interface TariffsClient {

    @GetMapping(value = "/{id}/current", produces = MediaType.APPLICATION_JSON_VALUE)
    TariffResponse getCurrentVersionOfTariffById(@PathVariable(value = "id") String id);

}