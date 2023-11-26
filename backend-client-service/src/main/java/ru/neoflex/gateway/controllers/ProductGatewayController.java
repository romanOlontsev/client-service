package ru.neoflex.gateway.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.neoflex.gateway.models.requests.ProductRequest;
import ru.neoflex.gateway.models.responses.ApiErrorResponse;
import ru.neoflex.gateway.models.responses.ProductResponse;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping("/api/products")
@Validated
public interface ProductGatewayController {

    @Operation(summary = "Get the current version of product")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "The product were successfully received",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ProductResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "The product not found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiErrorResponse.class)
                    )
            )
    })
    @GetMapping(value = "/{id}/versions/current", produces = MediaType.APPLICATION_JSON_VALUE)
    ProductResponse getCurrentVersionOfProductById(
            HttpServletRequest httpRequest,
            @Parameter(in = ParameterIn.PATH,
                    required = true)
            @PathVariable(value = "id") String id);

    @Operation(summary = "Get the previous versions of product")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "The product were successfully received",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(
                                    schema = @Schema(implementation = ProductResponse.class)
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "The product not found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiErrorResponse.class)
                    )
            )
    })
    @GetMapping(value = "/{id}/versions/previous", produces = MediaType.APPLICATION_JSON_VALUE)
    List<ProductResponse> getPreviousVersionsOfProductById(
            HttpServletRequest httpRequest,
            @Parameter(in = ParameterIn.PATH,
                    required = true)
            @PathVariable(value = "id") String id);

    @Operation(summary = "Get the version of product for a certain period")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "The product were successfully received",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ProductResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "The product not found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiErrorResponse.class)
                    )
            )
    })
    @GetMapping(value = "/{id}/versions/period", produces = MediaType.APPLICATION_JSON_VALUE)
    ProductResponse getVersionsOfProductForCertainPeriodById(
            HttpServletRequest httpRequest,
            @Parameter(in = ParameterIn.PATH,
                    required = true)
            @PathVariable(value = "id") String id,
            @Parameter(in = ParameterIn.QUERY,
                    required = true)
            @RequestParam(value = "datetime")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime dateTime);


    @Operation(summary = "Create product")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "The product was created successfully"
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
    void createProduct(
            HttpServletRequest httpRequest,
            @Parameter(in = ParameterIn.DEFAULT,
                    schema = @Schema(implementation = ProductRequest.class))
            @RequestBody @Valid ProductRequest request);


    @Operation(summary = "Rollback a product version")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Rollback successful"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "The product not found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiErrorResponse.class)
                    )
            )
    })
    @PutMapping(value = "/{id}/rollback")
    void rollBackProductVersion(
            HttpServletRequest httpRequest,
            @Parameter(in = ParameterIn.PATH,
                    required = true)
            @PathVariable(value = "id") String id);


    @Operation(summary = "Update a product")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Update successful"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "The product not found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiErrorResponse.class)
                    )
            )
    })
    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    void updateProduct(
            HttpServletRequest httpRequest,
            @Parameter(in = ParameterIn.PATH,
                    required = true)
            @PathVariable(value = "id") String id,
            @Parameter(in = ParameterIn.DEFAULT,
                    schema = @Schema(implementation = ProductRequest.class))
            @RequestBody ProductRequest request);

    @Operation(summary = "Delete product")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "The product was deleted successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Product not found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiErrorResponse.class)
                    )
            )
    })
    @DeleteMapping("/{id}")
    void deleteProduct(
            HttpServletRequest httpRequest,
            @Parameter(in = ParameterIn.PATH,
                    required = true)
            @PathVariable(value = "id") String id);

}

