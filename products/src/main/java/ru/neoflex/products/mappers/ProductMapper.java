package ru.neoflex.products.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.neoflex.products.models.entities.Product;
import ru.neoflex.products.models.requests.ProductRequest;
import ru.neoflex.products.models.responses.ProductResponse;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProductMapper {

    @Mapping(target = "tariff", ignore = true)
    ProductResponse productResponseFromProduct(Product product);

    Product productFromProductRequest(ProductRequest request);

    List<ProductResponse> productResponseListFromProductList(List<Product> productList);

    void updateProductFromProductRequest(@MappingTarget Product product, ProductRequest request);

    @Mapping(target = "productVersion", ignore = true)
    @Mapping(target = "tariffVersion", ignore = true)
    @Mapping(target = "tariff", ignore = true)
    void updateProduct(@MappingTarget Product productTarget, Product source);
}
