package ru.neoflex.products.configuration;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static java.nio.charset.Charset.defaultCharset;
import static org.springframework.util.StreamUtils.copyToString;

public class TariffsMocks {
    public static void setupMockResponseForCurrentVersion(WireMockServer mockService, String tariffId, String resourcePath) throws IOException {
        mockService.stubFor(get(urlEqualTo(String.format("/api/tariffs/%s/versions/current", tariffId)))
                .willReturn(
                        aResponse()
                                .withStatus(HttpStatus.OK.value())
                                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                .withBody(copyToString(TariffsMocks.class.getClassLoader()
                                                                         .getResourceAsStream(resourcePath),
                                        defaultCharset()))));
    }

    public static void setupMockResponseByVersion(WireMockServer mockService, String tariffId, String version, String resourcePath) throws IOException {
        mockService.stubFor(get(urlEqualTo(String.format("/api/tariffs/%s/versions/%s", tariffId, version)))
                .willReturn(
                        aResponse()
                                .withStatus(HttpStatus.OK.value())
                                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                .withBody(copyToString(TariffsMocks.class.getClassLoader()
                                                                         .getResourceAsStream(resourcePath),
                                        defaultCharset()))));
    }
}
