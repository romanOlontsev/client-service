package ru.neoflex.auth.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.neoflex.auth.models.requests.SigninRequest;
import ru.neoflex.auth.models.requests.SignupRequest;
import ru.neoflex.auth.models.responses.ApiErrorResponse;
import ru.neoflex.auth.models.responses.JwtAuthenticationResponse;
import ru.neoflex.auth.models.responses.TokenStatusResponse;
import ru.neoflex.auth.models.responses.UserDetailsResponse;

@RequestMapping("/api/auth")
@SecurityRequirement(name = "Bearer Authentication")
public interface AuthController {

    @Operation(summary = "Registration new user")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User successfully added"),
            @ApiResponse(
                    responseCode = "409",
                    description = "User already exists",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class))),
    })
    @PostMapping("/signup")
    JwtAuthenticationResponse signup(@RequestBody SignupRequest request);

    @Operation(summary = "Authorization successfully")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User successfully added"),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class))),
    })
    @PostMapping("/signin")
    JwtAuthenticationResponse signin(@RequestBody SigninRequest request);


    @Operation(summary = "Check token for validity")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Token is valid",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UserDetailsResponse.class)))
    })
    @GetMapping(value = "/token/status", produces = MediaType.APPLICATION_JSON_VALUE)
    TokenStatusResponse checkTokenForValidity(@RequestParam("token") String token);
}