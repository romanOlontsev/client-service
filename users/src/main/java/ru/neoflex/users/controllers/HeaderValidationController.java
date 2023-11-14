package ru.neoflex.users.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.neoflex.users.models.requests.HeaderValidationRequest;
import ru.neoflex.users.models.responses.ApiErrorResponse;
import ru.neoflex.users.models.responses.HeaderValidationResponse;

import java.util.List;

@RestController
@RequestMapping("/admin/validation")
@Validated
public interface HeaderValidationController {

    @Operation(summary = "Get header validation rules")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "The rules were successfully received",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(
                                    schema = @Schema(implementation = HeaderValidationResponse.class)
                            )
                    )
            )
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    List<HeaderValidationResponse> getHeaderValidationList();

    @Operation(summary = "Add header and required fields")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "The rule was created successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request parameters",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Header already exists",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiErrorResponse.class)
                    )
            )
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    void addHeaderValidation(
            @Parameter(in = ParameterIn.DEFAULT,
                    schema = @Schema(implementation = HeaderValidationRequest.class))
            @RequestBody
            @Valid HeaderValidationRequest request);

    @Operation(summary = "Update required fields for header")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "The rule was updated successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request parameters",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Header not found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiErrorResponse.class)
                    )
            )
    })
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    void updateHeaderValidation(
            @Parameter(in = ParameterIn.DEFAULT,
                    schema = @Schema(implementation = HeaderValidationRequest.class))
            @RequestBody
            @Valid HeaderValidationRequest request);

    @Operation(summary = "Delete header and required fields")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "The rule was deleted successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Header not found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiErrorResponse.class)
                    )
            )
    })
    @DeleteMapping("/{header}")
    void deleteHeaderValidation(
            @Parameter(in = ParameterIn.PATH,
                    required = true)
            @PathVariable(value = "header") String header);

}
