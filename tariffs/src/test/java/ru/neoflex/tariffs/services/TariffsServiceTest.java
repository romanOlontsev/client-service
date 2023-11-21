package ru.neoflex.tariffs.services;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.maciejwalkowiak.wiremock.spring.ConfigureWireMock;
import com.maciejwalkowiak.wiremock.spring.EnableWireMock;
import com.maciejwalkowiak.wiremock.spring.InjectWireMock;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import ru.neoflex.tariffs.configuration.IntegrationEnvironment;
import ru.neoflex.tariffs.configuration.ProductsMocks;
import ru.neoflex.tariffs.configuration.TestDbConfiguration;
import ru.neoflex.tariffs.exceptions.BadRequestException;
import ru.neoflex.tariffs.exceptions.DataNotFoundException;
import ru.neoflex.tariffs.models.entities.Tariff;
import ru.neoflex.tariffs.models.requests.TariffRequest;
import ru.neoflex.tariffs.models.responses.TariffResponse;
import ru.neoflex.tariffs.repositories.TariffRepository;

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
        @ConfigureWireMock(name = "products-service", property = "products.client.base-url")
})
class TariffsServiceTest extends IntegrationEnvironment {

    @Autowired
    private TestDbConfiguration.DbInfo info;

    @Autowired
    private TariffsService service;

    @Autowired
    private TariffRepository repository;

    @InjectWireMock("products-service")
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
    void getCurrentVersionOfTariffById_shouldReturnExpectedTariffResponse() {
        UUID tariffId = UUID.fromString("a32b8ba4-2dce-437c-ae94-34563b7ada43");
        TariffResponse expected = TariffResponse.builder()
                                                .id(tariffId)
                                                .name("tariff_1")
                                                .startDate(LocalDateTime.of(2023, 11, 21, 12, 40, 40))
                                                .endDate(LocalDateTime.of(2023, 11, 22, 12, 40, 40))
                                                .description("description")
                                                .rate(123.321)
                                                .version(2L)
                                                .build();

        TariffResponse response = service.getCurrentVersionOfTariffById(tariffId.toString());

        assertAll(
                () -> assertThat(response).isNotNull()
                                          .isEqualTo(expected)
                 );
    }

    @Test
    void getCurrentVersionOfTariffById_shouldThrowDataNotFoundException() {
        String tariffId = UUID.randomUUID()
                              .toString();
        assertAll(
                () -> assertThatThrownBy(() -> service.getCurrentVersionOfTariffById(tariffId))
                        .isInstanceOf(DataNotFoundException.class)
                        .hasMessage(String.format("Tariff with id=%s not found", tariffId))
                 );
    }

    @Test
    void getPreviousVersionsOfTariffById_shouldReturnExpectedList() {
        UUID tariffId = UUID.fromString("a32b8ba4-2dce-437c-ae94-34563b7ada43");
        TariffResponse expected = TariffResponse.builder()
                                                .id(tariffId)
                                                .name("tariff_1")
                                                .startDate(LocalDateTime.of(2023, 11, 21, 12, 40, 40))
                                                .endDate(LocalDateTime.of(2023, 11, 22, 12, 40, 40))
                                                .description("desc")
                                                .rate(123.321)
                                                .version(1L)
                                                .build();
        List<TariffResponse> expectedList = List.of(expected);

        List<TariffResponse> response = service.getPreviousVersionsOfTariffById(tariffId.toString());

        assertAll(
                () -> assertThat(response).isNotNull()
                                          .isNotEmpty()
                                          .hasSize(1)
                                          .isEqualTo(expectedList)
                 );
    }

    @Test
    void getPreviousVersionsOfTariffById_shouldReturnEmptyList() {
        UUID tariffId = UUID.fromString("5d033d70-712e-4bb9-abae-ba63f87a0b98");

        List<TariffResponse> response = service.getPreviousVersionsOfTariffById(tariffId.toString());

        assertAll(
                () -> assertThat(response).isNotNull()
                                          .isEmpty()
                 );
    }

    @Test
    void getTariffByIdAndVersion_shouldReturnExpectedTariff() {
        UUID tariffId = UUID.fromString("a32b8ba4-2dce-437c-ae94-34563b7ada43");
        TariffResponse expected = TariffResponse.builder()
                                                .id(tariffId)
                                                .name("tariff_1")
                                                .startDate(LocalDateTime.of(2023, 11, 21, 12, 40, 40))
                                                .endDate(LocalDateTime.of(2023, 11, 22, 12, 40, 40))
                                                .description("desc")
                                                .rate(123.321)
                                                .version(1L)
                                                .build();

        TariffResponse response = service.getTariffByIdAndVersion(tariffId.toString(), 1L);

        assertAll(
                () -> assertThat(response).isNotNull()
                                          .isEqualTo(expected)
                 );
    }

    @Test
    void getTariffByIdAndVersion_shouldThrowDataNotFoundException() {
        String tariffId = "a32b8ba4-2dce-437c-ae94-34563b7ada43";
        Long version = -1L;

        assertAll(
                () -> assertThatThrownBy(() -> service.getTariffByIdAndVersion(tariffId, version))
                        .isInstanceOf(DataNotFoundException.class)
                        .hasMessage(String.format("Tariff with id=%s and version %s not found", tariffId, version))
                 );
    }

    @Test
    void createTariff_shouldSaveResponse() {
        TariffRequest request = TariffRequest.builder()
                                             .name("test")
                                             .rate(23.212)
                                             .build();

        service.createTariff(request);
        List<Tariff> allTariffs = repository.findAll();

        assertAll(
                () -> assertThat(allTariffs).isNotEmpty()
                                            .hasSize(3),
                () -> assertThat(allTariffs.get(2)).extracting(Tariff::getName)
                                                   .isEqualTo(request.getName()),
                () -> assertThat(allTariffs.get(2)).extracting(Tariff::getRate)
                                                   .isEqualTo(request.getRate())

                 );
    }

    @Test
    void deleteTariffById_shouldDeleteTariff() throws IOException {
        TariffRequest request = TariffRequest.builder()
                                             .name("~test")
                                             .build();
        service.createTariff(request);
        List<Tariff> allTariffs = repository.findAll();
        Tariff tariffToDelete = allTariffs.stream()
                                          .filter(it -> it.getName()
                                                          .equals(request.getName()))
                                          .findFirst()
                                          .get();
        String tariffId = tariffToDelete.getId()
                                        .toString();

        ProductsMocks.setupMockResponse(wiremock, tariffId, "payload/response_is_empty.json");
        service.deleteTariffById(tariffId);
        List<Tariff> tariffs = repository.findAll();

        assertAll(
                () -> assertThat(tariffs).isNotEmpty()
                                         .hasSize(2),
                () -> assertThat(tariffs.get(0)).extracting(Tariff::getName)
                                                .isNotEqualTo(request.getName()),
                () -> assertThat(tariffs.get(1)).extracting(Tariff::getName)
                                                .isNotEqualTo(request.getName())

                 );
    }

    @Test
    void deleteTariffById_shouldThrowBadRequestException() throws IOException {
        String tariffId = "a32b8ba4-2dce-437c-ae94-34563b7ada43";

        ProductsMocks.setupMockResponse(wiremock, tariffId, "payload/product_response.json");

        assertAll(
                () -> assertThatThrownBy(() -> service.deleteTariffById(tariffId))
                        .isInstanceOf(BadRequestException.class)
                        .hasMessage(String.format("There are products that refer to a tariff with an id=%s", tariffId))
                 );
    }
}