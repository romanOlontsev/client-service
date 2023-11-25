package ru.neoflex.tariffs.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.neoflex.tariffs.models.requests.TariffRequest;
import ru.neoflex.tariffs.models.responses.ApiErrorResponse;
import ru.neoflex.tariffs.models.responses.TariffResponse;

import java.util.List;

@RestController
@RequestMapping("/api/tariffs")
@SecurityRequirement(name = "Bearer Authentication")
@Validated
public interface TariffsController {

    @Operation(summary = "Get the current version of tariff")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "The tariff was successfully received",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = TariffResponse.class)
                    )
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
    @GetMapping(value = "/{id}/versions/current", produces = MediaType.APPLICATION_JSON_VALUE)
    TariffResponse getCurrentVersionOfTariffById(
            @Parameter(in = ParameterIn.PATH,
                    required = true)
            @PathVariable(value = "id") String id);

    @Operation(summary = "Get the version of tariff by version number")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "The version of tariff was successfully received",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = TariffResponse.class)
                    )
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
    @GetMapping(value = "{id}/versions/{version}", produces = MediaType.APPLICATION_JSON_VALUE)
    TariffResponse getTariffByIdAndVersion(
            @Parameter(in = ParameterIn.PATH,
                    required = true)
            @PathVariable(value = "id") String id,
            @Parameter(in = ParameterIn.PATH,
                    required = true)
            @PathVariable(value = "version") Long version);

    @Operation(summary = "Get the previous versions of tariff")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "The tariffs were successfully received",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(
                                    schema = @Schema(implementation = TariffResponse.class)
                            )
                    )
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
    @GetMapping(value = "/{id}/versions/previous", produces = MediaType.APPLICATION_JSON_VALUE)
    List<TariffResponse> getPreviousVersionsOfTariffById(
            @Parameter(in = ParameterIn.PATH,
                    required = true)
            @PathVariable(value = "id") String id);


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


    @Operation(summary = "Update a tariff")
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
