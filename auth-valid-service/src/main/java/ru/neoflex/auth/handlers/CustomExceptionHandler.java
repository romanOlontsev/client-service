package ru.neoflex.auth.handlers;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.neoflex.auth.exceptions.*;
import ru.neoflex.auth.models.responses.ApiErrorResponse;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String message = ex.getFieldErrors()
                           .stream()
                           .map(it -> it.getField() + ": " + it.getDefaultMessage())
                           .collect(Collectors.joining("; "));
        ApiErrorResponse apiErrorResponse = getApiErrorResponse(ex, "400", message);
        log.error(message);
        return new ResponseEntity<>(apiErrorResponse, status);
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handle(BadRequestException e) {
        String message = e.getMessage();
        log.error(message);
        return getApiErrorResponse(e, "400", message);
    }

    @ExceptionHandler({
            UsernameNotFoundException.class,
            ExpiredJwtException.class,
            InvalidTokenException.class,
            AuthenticationException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiErrorResponse handle(RuntimeException e) {
        String message = e.getMessage();
        log.error(message);
        return getApiErrorResponse(e, "401", message);
    }

    @ExceptionHandler({
            UnregisteredAddressException.class,
            ForbiddenException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiErrorResponse handle(ForbiddenException e) {
        String message = e.getMessage();
        log.error(message);
        return getApiErrorResponse(e, "403", message);
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