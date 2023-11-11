package ru.neoflex.clientservice.handlers;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.neoflex.clientservice.exceptions.BadRequestException;
import ru.neoflex.clientservice.exceptions.DataAlreadyExistsException;
import ru.neoflex.clientservice.exceptions.DataNotFoundException;
import ru.neoflex.clientservice.models.responses.ApiErrorResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handle(MethodArgumentNotValidException e) {
        String message = e.getFieldErrors()
                          .stream()
                          .map(it -> it.getField() + ": " + it.getDefaultMessage())
                          .collect(Collectors.joining("; "));
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

    @ExceptionHandler(DataAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiErrorResponse handle(DataAlreadyExistsException e) {
        String message = e.getMessage();
        log.error(message);
        return getApiErrorResponse(e, "409", message);
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