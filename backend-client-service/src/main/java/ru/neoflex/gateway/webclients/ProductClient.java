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

    @GetMapping(value = "/{id}/current", produces = MediaType.APPLICATION_JSON_VALUE)
    ProductResponse getCurrentVersionOfProductById(@PathVariable(value = "id") String id);

    @GetMapping(value = "/{id}/previous", produces = MediaType.APPLICATION_JSON_VALUE)
    List<ProductResponse> getPreviousVersionsOfProductById(@PathVariable(value = "id") String id);

    @GetMapping(value = "/{id}/period", produces = MediaType.APPLICATION_JSON_VALUE)
    ProductResponse getVersionsOfProductForCertainPeriodById(@PathVariable(value = "id") String id,
                                                             @RequestParam(value = "datetime")
                                                             @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                             LocalDateTime dateTime);

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    void createProduct(@RequestBody ProductRequest request);

    @PutMapping(value = "/{id}/rollback")
    void rollBackProductVersion(@PathVariable(value = "id") String id);

    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    void updateProduct(@PathVariable(value = "id") String id, @RequestBody ProductRequest request);

    @DeleteMapping("/{id}")
    void deleteProduct(@PathVariable(value = "id") String id);

}