package ru.neoflex.gateway.webclients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.neoflex.gateway.models.requests.ProductRequest;
import ru.neoflex.gateway.models.responses.ProductResponse;

import java.time.LocalDateTime;
import java.util.List;

@FeignClient(value = "products", url = "${products.client.base-url}", configuration = CustomErrorDecoder.class)
public interface ProductClient {

    @GetMapping(value = "/api/products/{id}/current", produces = MediaType.APPLICATION_JSON_VALUE)
    ProductResponse getCurrentVersionOfProductById(
            @RequestHeader("Authorization") String token,
            @PathVariable(value = "id") String id);

    @GetMapping(value = "/api/products/{id}/previous", produces = MediaType.APPLICATION_JSON_VALUE)
    List<ProductResponse> getPreviousVersionsOfProductById(
            @RequestHeader("Authorization") String token,
            @PathVariable(value = "id") String id);

    @GetMapping(value = "/api/products/{id}/period", produces = MediaType.APPLICATION_JSON_VALUE)
    ProductResponse getVersionsOfProductForCertainPeriodById(
            @RequestHeader("Authorization") String token,
            @PathVariable(value = "id") String id,
            @RequestParam(value = "datetime")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime dateTime);

    @PostMapping(value = "/api/products", consumes = MediaType.APPLICATION_JSON_VALUE)
    void createProduct(
            @RequestHeader("Authorization") String token,
            @RequestBody ProductRequest request);

    @PutMapping(value = "/api/products/{id}/rollback")
    void rollBackProductVersion(
            @RequestHeader("Authorization") String token,
            @PathVariable(value = "id") String id);

    @PatchMapping(value = "/api/products/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    void updateProduct(
            @RequestHeader("Authorization") String token,
            @PathVariable(value = "id") String id, @RequestBody ProductRequest request);

    @DeleteMapping("/api/products/{id}")
    void deleteProduct(
            @RequestHeader("Authorization") String token,
            @PathVariable(value = "id") String id);

}