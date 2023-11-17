package ru.neoflex.gateway.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.neoflex.gateway.models.requests.ProductRequest;
import ru.neoflex.gateway.models.requests.TariffRequest;
import ru.neoflex.gateway.models.responses.ApiErrorResponse;
import ru.neoflex.gateway.models.responses.ProductResponse;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/tariff")
@Validated
public interface TariffGatewayController {

    @Operation(summary = "Create tariff")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "The tariff was created successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request parameters",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiErrorResponse.class)
                    )
            )
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    void createTariff(
            @Parameter(in = ParameterIn.DEFAULT,
                    schema = @Schema(implementation = TariffRequest.class))
            @RequestBody TariffRequest request);


    @Operation(summary = "Update a tariff version")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Update successful"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "The tariff not found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiErrorResponse.class)
                    )
            )
    })
    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    void updateTariff(
            @Parameter(in = ParameterIn.PATH,
                    required = true)
            @PathVariable(value = "id") String id,
            @Parameter(in = ParameterIn.DEFAULT,
                    schema = @Schema(implementation = TariffRequest.class))
            @RequestBody TariffRequest request);

    @Operation(summary = "Delete tariff")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "The tariff was deleted successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Tariff not found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiErrorResponse.class)
                    )
            )
    })
    @DeleteMapping("/{id}")
    void deleteTariff(
            @Parameter(in = ParameterIn.PATH,
                    required = true)
            @PathVariable(value = "id") String id);
}

