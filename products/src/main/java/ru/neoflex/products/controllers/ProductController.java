package ru.neoflex.products.controllers;

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
import ru.neoflex.products.models.requests.ProductRequest;
import ru.neoflex.products.models.responses.ApiErrorResponse;
import ru.neoflex.products.models.responses.ProductResponse;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/product")
@Validated
public interface ProductController {

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
    @GetMapping(value = "/{id}/current", produces = MediaType.APPLICATION_JSON_VALUE)
    ProductResponse getCurrentVersionOfProductById(
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
    @GetMapping(value = "/{id}/previous", produces = MediaType.APPLICATION_JSON_VALUE)
    List<ProductResponse> getPreviousVersionsOfProductById(
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
    @GetMapping(value = "/{id}/period", produces = MediaType.APPLICATION_JSON_VALUE)
    ProductResponse getVersionsOfProductForCertainPeriodById(
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
            @Parameter(in = ParameterIn.DEFAULT,
                    schema = @Schema(implementation = ProductRequest.class))
            @RequestBody ProductRequest request);

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
            @Parameter(in = ParameterIn.PATH,
                    required = true)
            @PathVariable(value = "id") String id);

    @Operation(summary = "Update a product version")
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
            @Parameter(in = ParameterIn.PATH,
                    required = true)
            @PathVariable(value = "id") String id);

}
