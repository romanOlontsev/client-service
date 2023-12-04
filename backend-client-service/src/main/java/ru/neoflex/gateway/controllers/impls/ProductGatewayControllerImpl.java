package ru.neoflex.gateway.controllers.impls;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import ru.neoflex.gateway.controllers.ProductGatewayController;
import ru.neoflex.gateway.models.requests.ProductRequest;
import ru.neoflex.gateway.models.responses.ProductResponse;
import ru.neoflex.gateway.services.ProductGatewayService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ProductGatewayControllerImpl implements ProductGatewayController {

    private final ProductGatewayService service;

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
    public void createProduct(HttpServletRequest httpRequest, ProductRequest request) {
        service.createProduct(httpRequest, request);
    }

    @Override
    public void rollBackProductVersion(HttpServletRequest httpRequest, String id) {
        service.rollBackProductVersionById(httpRequest, id);
    }

    @Override
    public void updateProduct(HttpServletRequest httpRequest, String id, ProductRequest request) {
        service.updateProduct(httpRequest, id, request);
    }

    @Override
    public void deleteProduct(HttpServletRequest httpRequest, String id) {
        service.deleteProductById(httpRequest, id);
    }

}
