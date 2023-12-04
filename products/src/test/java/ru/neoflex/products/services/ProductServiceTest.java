package ru.neoflex.products.services;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.maciejwalkowiak.wiremock.spring.ConfigureWireMock;
import com.maciejwalkowiak.wiremock.spring.EnableWireMock;
import com.maciejwalkowiak.wiremock.spring.InjectWireMock;
import jakarta.servlet.http.HttpServletRequest;
import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.DirectoryResourceAccessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import ru.neoflex.products.configuration.IntegrationEnvironment;
import ru.neoflex.products.configuration.TariffsMocks;
import ru.neoflex.products.configuration.TestDbConfiguration;
import ru.neoflex.products.exceptions.DataNotFoundException;
import ru.neoflex.products.models.entities.Product;
import ru.neoflex.products.models.requests.ProductRequest;
import ru.neoflex.products.models.responses.ProductResponse;
import ru.neoflex.products.models.responses.TariffResponse;
import ru.neoflex.products.repositories.ProductRepository;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;


@SpringBootTest
@EnableFeignClients
@Rollback
@Transactional
@EnableWireMock({
        @ConfigureWireMock(name = "tariffs-service", property = "tariffs.client.base-url")
})
class ProductServiceTest extends IntegrationEnvironment {

    @Autowired
    private TestDbConfiguration.DbInfo info;

    @Autowired
    private ProductService service;

    @Autowired
    private ProductRepository repository;

    @InjectWireMock("tariffs-service")
    private WireMockServer wiremock;

    @BeforeEach
    void setUp() {
        try (Connection connection = DriverManager.getConnection(info.getUrl(), info.getUsername(), info.getPassword());
             Database database = DatabaseFactory.getInstance()
                                                .findCorrectDatabaseImplementation(new JdbcConnection(connection))) {
            Liquibase liquibase = new liquibase.Liquibase("master.xml",
                                                          new DirectoryResourceAccessor(new File("").toPath()
                                                                                                    .toAbsolutePath()
                                                                                                    .getParent()
                                                                                                    .resolve("migrations")), database
            );
            liquibase.update(new Contexts(), new LabelExpression());
        } catch (LiquibaseException | SQLException | FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    void getProductsByTariffId_shouldReturnExpectedList() {
        String tariffId = "a32b8ba4-2dce-437c-ae94-34563b7ada43";
//        TariffResponse tariffResponse = TariffResponse.builder()
//                                                      .id(UUID.fromString(tariffId))
//                                                      .name("tariff_1")
//                                                      .startDate(LocalDateTime.of(2023, 11, 21, 12, 40, 40))
//                                                      .endDate(LocalDateTime.of(2023, 11, 22, 12, 40, 40))
//                                                      .description("description")
//                                                      .rate(123.321)
//                                                      .version(2L)
//                                                      .build();
        ProductResponse productResponse = ProductResponse.builder()
                                                         .id(UUID.fromString("6fb8f572-e70d-4416-ac09-214174d04b52"))
                                                         .name("product_1")
                                                         .type("LOAN")
                                                         .startDate(LocalDateTime.of(2023, 11, 21, 12, 40, 40))
                                                         .endDate(LocalDateTime.of(2023, 11, 22, 12, 40, 40))
                                                         .description("description")
                                                         .productVersion(2L)
                                                         .build();
        List<ProductResponse> expectedList = List.of(productResponse);

        List<ProductResponse> response = service.getProductsByTariffId(tariffId);

        assertAll(
                () -> assertThat(response).isNotEmpty()
                                          .hasSize(1)
                                          .isEqualTo(expectedList)

                 );

    }

    @Test
    void getCurrentVersionOfProductById_shouldExpectedResponse() throws IOException {
        String tariffId = "a32b8ba4-2dce-437c-ae94-34563b7ada43";
        TariffResponse tariffResponse = TariffResponse.builder()
                                                      .id(UUID.fromString(tariffId))
                                                      .name("tariff_1")
                                                      .startDate(LocalDateTime.of(2023, 11, 21, 12, 40, 40))
                                                      .endDate(LocalDateTime.of(2023, 11, 22, 12, 40, 40))
                                                      .description("description")
                                                      .rate(123.321)
                                                      .version(2L)
                                                      .build();
        String productId = "6fb8f572-e70d-4416-ac09-214174d04b52";
        ProductResponse expected = ProductResponse.builder()
                                                  .id(UUID.fromString(productId))
                                                  .name("product_1")
                                                  .type("LOAN")
                                                  .startDate(LocalDateTime.of(2023, 11, 21, 12, 40, 40))
                                                  .endDate(LocalDateTime.of(2023, 11, 22, 12, 40, 40))
                                                  .description("description")
                                                  .tariff(tariffResponse)
                                                  .productVersion(2L)
                                                  .build();
        HttpServletRequest httpRequestMock = Mockito.mock(HttpServletRequest.class);

        TariffsMocks.setupMockResponseForCurrentVersion(wiremock, tariffId, "payload/tariff_response.json");
        ProductResponse response = service.getCurrentVersionOfProductById(httpRequestMock, productId);

        assertAll(
                () -> assertThat(response).isNotNull()
                                          .isEqualTo(expected)

                 );
    }

    @Test
    void getCurrentVersionOfProductById_shouldThrowDataNotFoundException() {
        String productId = UUID.randomUUID()
                               .toString();
        HttpServletRequest httpRequestMock = Mockito.mock(HttpServletRequest.class);

        assertAll(
                () -> assertThatThrownBy(() -> service.getCurrentVersionOfProductById(httpRequestMock, productId))
                        .isInstanceOf(DataNotFoundException.class)
                        .hasMessage(String.format("Product with id=%s not found", productId))
                 );
    }

    @Test
    void getPreviousVersionsOfProductById_shouldReturnExpectedList() throws IOException {
        String tariffId = "a32b8ba4-2dce-437c-ae94-34563b7ada43";
        TariffResponse tariffResponse = TariffResponse.builder()
                                                      .id(UUID.fromString(tariffId))
                                                      .name("tariff_1")
                                                      .startDate(LocalDateTime.of(2023, 11, 21, 12, 40, 40))
                                                      .endDate(LocalDateTime.of(2023, 11, 22, 12, 40, 40))
                                                      .description("desc")
                                                      .rate(123.321)
                                                      .version(1L)
                                                      .build();
        String productId = "6fb8f572-e70d-4416-ac09-214174d04b52";
        ProductResponse expected = ProductResponse.builder()
                                                  .id(UUID.fromString(productId))
                                                  .name("product_1")
                                                  .type("LOAN")
                                                  .startDate(LocalDateTime.of(2023, 11, 21, 12, 40, 40))
                                                  .endDate(LocalDateTime.of(2023, 11, 22, 12, 40, 40))
                                                  .description("description")
                                                  .tariff(tariffResponse)
                                                  .productVersion(1L)
                                                  .build();
        HttpServletRequest httpRequestMock = Mockito.mock(HttpServletRequest.class);
        List<ProductResponse> expectedList = List.of(expected);

        TariffsMocks.setupMockResponseByVersion(wiremock, tariffId, tariffResponse.getVersion()
                                                                                  .toString(), "payload/tariff_response_by_version.json");
        List<ProductResponse> response = service.getPreviousVersionsOfProductById(httpRequestMock, productId);

        assertAll(
                () -> assertThat(response).isNotNull()
                                          .isNotEmpty()
                                          .hasSize(1)
                                          .isEqualTo(expectedList)

                 );
    }

    @Test
    void getVersionOfProductForCertainPeriodById() throws IOException {
        String tariffId = "a32b8ba4-2dce-437c-ae94-34563b7ada43";
        TariffResponse tariffResponse = TariffResponse.builder()
                                                      .id(UUID.fromString(tariffId))
                                                      .name("tariff_1")
                                                      .startDate(LocalDateTime.of(2023, 11, 21, 12, 40, 40))
                                                      .endDate(LocalDateTime.of(2023, 11, 22, 12, 40, 40))
                                                      .description("desc")
                                                      .rate(123.321)
                                                      .version(1L)
                                                      .build();
        String productId = "6fb8f572-e70d-4416-ac09-214174d04b52";
        ProductResponse expected = ProductResponse.builder()
                                                  .id(UUID.fromString(productId))
                                                  .name("product_1")
                                                  .type("LOAN")
                                                  .startDate(LocalDateTime.of(2023, 11, 21, 12, 40, 40))
                                                  .endDate(LocalDateTime.of(2023, 11, 22, 12, 40, 40))
                                                  .description("description")
                                                  .tariff(tariffResponse)
                                                  .productVersion(1L)
                                                  .build();
        HttpServletRequest httpRequestMock = Mockito.mock(HttpServletRequest.class);
        TariffsMocks.setupMockResponseByVersion(wiremock, tariffId, tariffResponse.getVersion()
                                                                                  .toString(), "payload/tariff_response_by_version.json");

        LocalDateTime localDateTime = LocalDateTime.of(2023, 10, 30, 12, 41, 0);
        ProductResponse response = service.getVersionOfProductForCertainPeriodById(httpRequestMock, productId, localDateTime);

        assertAll(
                () -> assertThat(response).isNotNull()
                                          .isEqualTo(expected)

                 );
    }

    @Test
    void getVersionOfProductForCertainPeriodById_shouldThrowDataNotFoundException() {
        String productId = UUID.randomUUID()
                               .toString();
        LocalDateTime localDateTime = LocalDateTime.of(2000, 9, 30, 21, 41, 0);
        HttpServletRequest httpRequestMock = Mockito.mock(HttpServletRequest.class);

        assertAll(
                () -> assertThatThrownBy(() -> service.getVersionOfProductForCertainPeriodById(httpRequestMock, productId, localDateTime))
                        .isInstanceOf(DataNotFoundException.class)
                        .hasMessage(String.format("Version for product with id=%s for period %s not found", productId, localDateTime))
                 );
    }

    @Test
    void createProduct_shouldSaveToDb() throws IOException {
        String tariffId = "a32b8ba4-2dce-437c-ae94-34563b7ada43";
        ProductRequest request = ProductRequest.builder()
                                               .name("test")
                                               .type("CARD")
                                               .tariff(UUID.fromString(tariffId))
                                               .build();
        TariffsMocks.setupMockResponseForCurrentVersion(wiremock, tariffId, "payload/tariff_response.json");
        HttpServletRequest httpRequestMock = Mockito.mock(HttpServletRequest.class);

        service.createProduct(httpRequestMock, request);
        List<Product> allTariffs = repository.findAll();

        assertAll(
                () -> assertThat(allTariffs).isNotEmpty()
                                            .hasSize(3),
                () -> assertThat(allTariffs.get(2)).extracting(Product::getName)
                                                   .isEqualTo(request.getName()),
                () -> assertThat(allTariffs.get(2)).extracting(Product::getType)
                                                   .isEqualTo(request.getType())
                 );
    }

    @Test
    void rollBackProductVersionById_shouldRollbackVersionById() {
        String productId = "6fb8f572-e70d-4416-ac09-214174d04b52";
        ProductRequest updated = ProductRequest.builder()
                                               .name("GUS")
                                               .description("test")
                                               .build();
        HttpServletRequest httpRequestMock = Mockito.mock(HttpServletRequest.class);
        service.updateProduct(httpRequestMock, productId, updated);

        service.rollBackProductVersionById(productId);
        List<Product> response = repository.findAll();
        Product foundProduct = response.stream()
                                       .filter(it -> it.getId()
                                                       .equals(UUID.fromString(productId)))
                                       .findFirst()
                                       .get();


        assertAll(
                () -> assertThat(response).isNotNull()
                                          .isNotEmpty()
                                          .hasSize(2),
                () -> assertThat(foundProduct).isNotNull()
                                              .extracting(Product::getName)
                                              .isNotEqualTo(updated.getName()),
                () -> assertThat(foundProduct).isNotNull()
                                              .extracting(Product::getTariffVersion)
                                              .isNotEqualTo(updated.getDescription())
                 );
    }

    @Test
    void rollBackProductVersionById_shouldThrowDataNotFoundException() {
        String productId = "50f11359-b8a9-460d-b538-f8dcf04c2085";

        assertAll(
                () -> assertThatThrownBy(() -> service.rollBackProductVersionById(productId))
                        .isInstanceOf(DataNotFoundException.class)
                        .hasMessage(String.format("Previous version for product with id=%s not found", productId))
                 );
    }

    @Test
    void updateProduct_shouldUpdateByRequest() throws IOException {
        String productId = "6fb8f572-e70d-4416-ac09-214174d04b52";
        String tariffId = "5d033d70-712e-4bb9-abae-ba63f87a0b98";
        ProductRequest updated = ProductRequest.builder()
                                               .name("GUS")
                                               .description("test")
                                               .tariff(UUID.fromString(tariffId))
                                               .build();

        HttpServletRequest httpRequestMock = Mockito.mock(HttpServletRequest.class);
        TariffsMocks.setupMockResponseForCurrentVersion(wiremock, tariffId, "payload/update_tariff_response.json");

        service.updateProduct(httpRequestMock, productId, updated);
        List<Product> response = repository.findAll();
        Product foundProduct = response.stream()
                                       .filter(it -> it.getId()
                                                       .equals(UUID.fromString(productId)))
                                       .findFirst()
                                       .get();


        assertAll(
                () -> assertThat(response).isNotNull()
                                          .isNotEmpty()
                                          .hasSize(2),
                () -> assertThat(foundProduct).isNotNull()
                                              .extracting(Product::getName)
                                              .isEqualTo(updated.getName()),
                () -> assertThat(foundProduct).isNotNull()
                                              .extracting(Product::getTariffVersion)
                                              .isEqualTo(1L),
                () -> assertThat(foundProduct).isNotNull()
                                              .extracting(Product::getTariff)
                                              .isEqualTo(updated.getTariff())
                 );
    }

    @Test
    void deleteProductById_shouldDeleteById() {
        String productId = "50f11359-b8a9-460d-b538-f8dcf04c2085";

        service.deleteProductById(productId);
        List<Product> response = repository.findAll();

        assertAll(
                () -> assertThat(response).isNotNull()
                                          .isNotEmpty()
                                          .hasSize(1),
                () -> assertThat(response.get(0)).extracting(Product::getId)
                                                 .isNotEqualTo(UUID.fromString(productId))
                 );
    }
}