package ru.neoflex.tariffs.webclients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.neoflex.tariffs.models.responses.ProductResponse;

import java.util.List;

@FeignClient(value = "products", url = "${products.client.base-url}")
public interface ProductClient {

    @GetMapping(value = "/api/products",produces = MediaType.APPLICATION_JSON_VALUE)
    List<ProductResponse> getProductsByTariffId(@RequestParam(value = "tariff") String tariffId);
}
