package ru.neoflex.tariffs.configuration;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static java.nio.charset.Charset.defaultCharset;
import static org.springframework.util.StreamUtils.copyToString;

public class ProductsMocks {
    public static void setupMockResponse(WireMockServer mockService, String tariffId, String resourcePath) throws IOException {
        mockService.stubFor(get(urlEqualTo("/api/products?tariff=" + tariffId))
                                    .willReturn(
                                            aResponse()
                                                    .withStatus(HttpStatus.OK.value())
                                                    .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                                    .withBody(copyToString(ProductsMocks.class.getClassLoader()
                                                                                              .getResourceAsStream(resourcePath),
                                                                           defaultCharset()))));
    }
}
