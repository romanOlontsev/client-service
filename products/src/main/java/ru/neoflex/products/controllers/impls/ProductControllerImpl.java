package ru.neoflex.products.controllers.impls;

import jakarta.servlet.http.HttpServletRequest;
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
    public ProductResponse getCurrentVersionOfProductById(HttpServletRequest httpRequest, String id) {
        return service.getCurrentVersionOfProductById(httpRequest, id);
    }

    @Override
    public List<ProductResponse> getPreviousVersionsOfProductById(HttpServletRequest httpRequest, String id) {
        return service.getPreviousVersionsOfProductById(httpRequest, id);
    }

    @Override
    public ProductResponse getVersionsOfProductForCertainPeriodById(HttpServletRequest httpRequest, String id, LocalDateTime dateTime) {
        return service.getVersionOfProductForCertainPeriodById(httpRequest, id, dateTime);
    }

    @Override
    public void createProduct(HttpServletRequest httpRequest, ProductRequest productRequest) {
        service.createProduct(httpRequest, productRequest);
    }

    @Override
    public void rollBackProductVersion(String id) {
        service.rollBackProductVersionById(id);
    }

    @Override
    public void updateProduct(HttpServletRequest httpRequest, String id, ProductRequest request) {
        service.updateProduct(httpRequest, id, request);
    }

    @Override
    public void deleteProduct(String id) {
        service.deleteProductById(id);
    }
}
