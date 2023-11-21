package ru.neoflex.products.controllers.impls;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import ru.neoflex.products.controllers.ProductController;
import ru.neoflex.products.models.requests.ProductRequest;
import ru.neoflex.products.models.responses.ProductResponse;
import ru.neoflex.products.services.ProductService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductControllerImpl implements ProductController {

    private final ProductService service;

    @Override
    public List<ProductResponse> getProductsByTariffId(String tariffId) {
        return service.getProductsByTariffId(tariffId);
    }

    @Override
    public ProductResponse getCurrentVersionOfProductById(String id) {
        return service.getCurrentVersionOfProductById(id);
    }

    @Override
    public List<ProductResponse> getPreviousVersionsOfProductById(String id) {
        return service.getPreviousVersionsOfProductById(id);
    }

    @Override
    public ProductResponse getVersionsOfProductForCertainPeriodById(String id, LocalDateTime dateTime) {
        return service.getVersionOfProductForCertainPeriodById(id, dateTime);
    }

    @Override
    public void createProduct(ProductRequest request) {
        service.createProduct(request);
    }

    @Override
    public void rollBackProductVersion(String id) {
        service.rollBackProductVersionById(id);
    }

    @Override
    public void updateProduct(String id, ProductRequest request) {
        service.updateProduct(id, request);
    }

    @Override
    public void deleteProduct(String id) {
        service.deleteProductById(id);
    }
}
