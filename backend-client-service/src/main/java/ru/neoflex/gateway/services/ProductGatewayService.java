package ru.neoflex.gateway.services;

import jakarta.servlet.http.HttpServletRequest;
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
public class ProductGatewayService {

    private final ProductClient productClient;

    public ProductResponse getCurrentVersionOfProductById(HttpServletRequest httpRequest, String id) {
        String token = getAuthorizationHeaderFromRequest(httpRequest);
        ProductResponse currentVersionOfProductById = productClient.getCurrentVersionOfProductById(token, id);
        log.info("Current version of product: {} has been received", currentVersionOfProductById);
        return currentVersionOfProductById;
    }

    public List<ProductResponse> getPreviousVersionsOfProductById(HttpServletRequest httpRequest, String id) {
        String token = getAuthorizationHeaderFromRequest(httpRequest);
        List<ProductResponse> previousVersionsOfProductById = productClient.getPreviousVersionsOfProductById(token, id);
        log.info("Previous versions of product: {} has been received", previousVersionsOfProductById);
        return previousVersionsOfProductById;
    }

    public ProductResponse getVersionOfProductForCertainPeriodById(HttpServletRequest httpRequest, String id, LocalDateTime dateTime) {
        String token = getAuthorizationHeaderFromRequest(httpRequest);
        ProductResponse versionsOfProductForCertainPeriod = productClient.getVersionsOfProductForCertainPeriodById(token, id, dateTime);
        log.info("Version of product for certain period: {} has been received", versionsOfProductForCertainPeriod);
        return versionsOfProductForCertainPeriod;
    }

    public void createProduct(HttpServletRequest httpRequest, ProductRequest request) {
        String token = getAuthorizationHeaderFromRequest(httpRequest);
        log.info("Request to save: {}", request);
        productClient.createProduct(token, request);
    }

    public void rollBackProductVersionById(HttpServletRequest httpRequest, String id) {
        String token = getAuthorizationHeaderFromRequest(httpRequest);
        log.info("Request to rollback for product with id={}", id);
        productClient.rollBackProductVersion(token, id);
    }

    public void updateProduct(HttpServletRequest httpRequest, String id, ProductRequest request) {
        String token = getAuthorizationHeaderFromRequest(httpRequest);
        log.info("Request to update for product with id={}", id);
        productClient.updateProduct(token, id, request);
    }

    public void deleteProductById(HttpServletRequest httpRequest, String id) {
        String token = getAuthorizationHeaderFromRequest(httpRequest);
        log.info("Request to delete for product with id={}", id);
        productClient.deleteProduct(token, id);
    }

    private String getAuthorizationHeaderFromRequest(HttpServletRequest httpRequest) {
        return httpRequest.getHeader("Authorization");
    }
}
