package ru.neoflex.products.handlers;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.neoflex.products.exceptions.BadRequestException;
import ru.neoflex.products.exceptions.DataNotFoundException;
import ru.neoflex.products.models.responses.ApiErrorResponse;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handle(ConstraintViolationException e) {
        Map<String, String> errorMap = new HashMap<>();
        e.getConstraintViolations()
         .forEach(it -> errorMap.put(it.getPropertyPath()
                                       .toString(), it.getMessage()));

        String message = e.getMessage() + errorMap;
        log.error(message);
        return getApiErrorResponse(e, "400", message);
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handle(BadRequestException e) {
        String message = e.getMessage();
        log.error(message);
        return getApiErrorResponse(e, "400", message);
    }

    @ExceptionHandler(SQLException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handle(SQLException e) {
        String message = e.getMessage();
        log.error(message);
        return getApiErrorResponse(e, "400", message);
    }

    @ExceptionHandler(DataNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrorResponse handle(DataNotFoundException e) {
        String message = e.getMessage();
        log.error(message);
        return getApiErrorResponse(e, "404", message);
    }

    private ApiErrorResponse getApiErrorResponse(Exception e, String code, String description) {
        ApiErrorResponse exceptionResponse = ApiErrorResponse.builder()
                                                             .code(code)
                                                             .description(description)
                                                             .exceptionName(e.getClass()
                                                                             .getName())
                                                             .exceptionMessage(e.getMessage())
                                                             .build();
        for (StackTraceElement stackTraceElement : e.getStackTrace()) {
            exceptionResponse.addStacktraceItem(stackTraceElement.toString());
        }
        return exceptionResponse;
    }
}