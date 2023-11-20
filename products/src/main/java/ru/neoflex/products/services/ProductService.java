package ru.neoflex.products.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.neoflex.products.exceptions.DataNotFoundException;
import ru.neoflex.products.mappers.ProductMapper;
import ru.neoflex.products.models.entities.Product;
import ru.neoflex.products.models.requests.ProductRequest;
import ru.neoflex.products.models.responses.ProductResponse;
import ru.neoflex.products.models.responses.TariffResponse;
import ru.neoflex.products.repositories.ProductRepository;
import ru.neoflex.products.webclients.TariffsClient;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository repository;

    private final ProductMapper mapper;

    private final TariffsClient tariffsClient;

    public ProductResponse getCurrentVersionOfProductById(String id) {
        Product foundProduct = findProductById(id);
        ProductResponse productResponse = mapper.productResponseFromProduct(foundProduct);
        if (foundProduct.getTariff() != null) {
            TariffResponse tariffResponse = getTariffByIdIfExists(foundProduct.getTariff());
            productResponse.setTariff(tariffResponse);
        }
        log.info("The current version of the product: {} has been received", productResponse);
        return productResponse;
    }

    @Transactional
    public List<ProductResponse> getPreviousVersionsOfProductById(String id) {
        List<Product> previousVersions = repository.findPreviousVersionsOfProductById(UUID.fromString(id));
        List<ProductResponse> responseList = new ArrayList<>();
        for (Product product : previousVersions) {
            ProductResponse productResponse = mapper.productResponseFromProduct(product);
            setTariffForProductByIdAndVersionIfExists(product, productResponse);
            responseList.add(productResponse);
        }
        log.info("Previous versions of the product: {} were received", responseList);
        return responseList;
    }

    @Transactional
    public ProductResponse getVersionOfProductForCertainPeriodById(String id, LocalDateTime dateTime) {
        Product foundProduct = repository.findVersionOfProductForCertainPeriodById(UUID.fromString(id), dateTime)
                                         .orElseThrow(() -> new DataNotFoundException(
                                                 String.format("Version for product with id=%s for period %s not found", id, dateTime)));
        ProductResponse productResponse = mapper.productResponseFromProduct(foundProduct);
        setTariffForProductByIdAndVersionIfExists(foundProduct, productResponse);
        log.info("Product version for the {} period: {} has been received", dateTime, productResponse);
        return productResponse;
    }

    public void createProduct(ProductRequest request) {
        Product product = mapper.productFromProductRequest(request);
        setTariffForProductByIdIfExists(product, request);
        Product savedProduct = repository.save(product);
        log.info("The product: {} has been saved", savedProduct);
    }

    @Transactional
    public void rollBackProductVersionById(String id) {
        Product foundProject = findProductById(id);
        Product previousVersion = repository.findPreviousVersionOfProductById(UUID.fromString(id))
                                            .orElseThrow(() -> new DataNotFoundException(
                                                    String.format("Previous version for product with id=%s not found", id)));
        mapper.updateProduct(foundProject, previousVersion);
        log.info("The product: {} has been rolled back", foundProject);
    }

    @Transactional
    public void updateProduct(String id, ProductRequest request) {
        Product foundProject = findProductById(id);
        setTariffForProductByIdIfExists(foundProject, request);
        mapper.updateProductFromProductRequest(foundProject, request);
        log.info("The product: {} has been updated", foundProject);
    }

    public void deleteProductById(String id) {
        Product foundProduct = findProductById(id);
        repository.delete(foundProduct);
        log.info("The product with id={} has been deleted", id);
    }

    private Product findProductById(String id) {
        return repository.findProjectById(UUID.fromString(id))
                         .orElseThrow(() -> new DataNotFoundException(
                                 String.format("Product with id=%s not found", id)));
    }

    private TariffResponse getTariffByIdIfExists(UUID tariffId) {
        return tariffsClient.getCurrentVersionOfTariffById(tariffId.toString());
    }

    private void setTariffForProductByIdAndVersionIfExists(Product foundProduct, ProductResponse productResponse) {
        if (foundProduct.getTariff() != null) {
            TariffResponse tariffResponse = tariffsClient.getTariffByIdAndVersion(
                    String.valueOf(foundProduct.getTariff()), foundProduct.getTariffVersion());
            productResponse.setTariff(tariffResponse);
        }
    }

    private void setTariffForProductByIdIfExists(Product product, ProductRequest request) {
        if (product.getTariff() == null && request.getTariff() != null) {
            UUID tariffId = request.getTariff();
            TariffResponse foundTariff = getTariffByIdIfExists(tariffId);
            product.setTariff(tariffId);
            product.setTariffVersion(foundTariff.getVersion());
        }
    }
}
