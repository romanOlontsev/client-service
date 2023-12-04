package ru.neoflex.tariffs.webclients;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import ru.neoflex.tariffs.exceptions.BadRequestException;
import ru.neoflex.tariffs.exceptions.DataNotFoundException;
import ru.neoflex.tariffs.exceptions.ForbiddenException;
import ru.neoflex.tariffs.models.responses.ApiErrorResponse;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class CustomErrorDecoder implements ErrorDecoder {
    private final ErrorDecoder errorDecoder;
    private final ObjectMapper objectMapper;

    public CustomErrorDecoder(ObjectMapper objectMapper) {
        this.errorDecoder = new Default();
        this.objectMapper = objectMapper;
    }

    @Override
    public Exception decode(String s, Response response) {

        String message = null;
        Reader reader = null;

        try {
            reader = response.body()
                             .asReader(StandardCharsets.UTF_8);
            String result = getResponseBodyAsString(response.body());
            objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            ApiErrorResponse exceptionMessage = objectMapper.readValue(result,
                                                                       ApiErrorResponse.class);
            message = exceptionMessage.getDescription();
        } catch (IOException e) {
            log.error("Failed to get body as reader: ", e);
        } finally {
            try {
                if (reader != null)
                    reader.close();
            } catch (IOException e) {
                log.error("Failed to close the reader with error: ", e);
            }
        }
        switch (response.status()) {
            case 400 -> throw new BadRequestException(message);
            case 401 -> throw new BadCredentialsException(message);
            case 403 -> throw new ForbiddenException(message);
            case 404 -> throw new DataNotFoundException(message);
            default -> {
                return errorDecoder.decode(s, response);
            }
        }
    }

    private String getResponseBodyAsString(final Response.Body body) {
        try {
            return IOUtils.toString(body.asReader(StandardCharsets.UTF_8));
        } catch (final IOException e) {
            log.error("Failed to read the response body with error: ", e);
        }
        return null;
    }
}