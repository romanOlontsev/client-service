package ru.neoflex.gateway.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.neoflex.gateway.models.requests.SigninRequest;
import ru.neoflex.gateway.models.requests.SignupRequest;
import ru.neoflex.gateway.models.responses.ApiErrorResponse;
import ru.neoflex.gateway.models.responses.JwtAuthenticationResponse;

@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping("/api/auth")
public interface AuthGatewayController {

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
}
