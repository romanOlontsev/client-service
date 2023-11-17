package ru.neoflex.gateway.controllers.impls;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import ru.neoflex.gateway.controllers.ProductGatewayController;
import ru.neoflex.gateway.models.requests.ProductRequest;
import ru.neoflex.gateway.models.responses.ProductResponse;
import ru.neoflex.gateway.services.GatewayService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ProductGatewayControllerImpl implements ProductGatewayController {

    private final GatewayService service;

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
    public void deleteProduct(String id) {
        service.deleteProductById(id);
    }

}
