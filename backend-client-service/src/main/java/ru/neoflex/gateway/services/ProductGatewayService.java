package ru.neoflex.gateway.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.neoflex.gateway.models.requests.ProductRequest;
import ru.neoflex.gateway.models.responses.ProductResponse;
import ru.neoflex.gateway.webclients.ProductClient;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class
ProductGatewayService {

    private final ProductClient productClient;

    public ProductResponse getCurrentVersionOfProductById(String id) {
        ProductResponse currentVersionOfProductById = productClient.getCurrentVersionOfProductById(id);
        log.info("Current version of product: {} has been received", currentVersionOfProductById);
        return currentVersionOfProductById;
    }

    public List<ProductResponse> getPreviousVersionsOfProductById(String id) {
        List<ProductResponse> previousVersionsOfProductById = productClient.getPreviousVersionsOfProductById(id);
        log.info("Previous versions of product: {} has been received", previousVersionsOfProductById);
        return previousVersionsOfProductById;
    }

    public ProductResponse getVersionOfProductForCertainPeriodById(String id, LocalDateTime dateTime) {
        ProductResponse versionsOfProductForCertainPeriod = productClient.getVersionsOfProductForCertainPeriodById(id, dateTime);
        log.info("Version of product for certain period: {} has been received", versionsOfProductForCertainPeriod);
        return versionsOfProductForCertainPeriod;
    }

    public void createProduct(ProductRequest request) {
        log.info("Request to save: {}", request);
        productClient.createProduct(request);
    }

    public void rollBackProductVersionById(String id) {
        log.info("Request to rollback for product with id={}", id);
        productClient.rollBackProductVersion(id);
    }

    public void updateProduct(String id, ProductRequest request) {
        log.info("Request to update for product with id={}", id);
        productClient.updateProduct(id, request);
    }

    public void deleteProductById(String id) {
        log.info("Request to delete for product with id={}", id);
        productClient.deleteProduct(id);
    }
}
