package ru.neoflex.auth.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.neoflex.auth.models.responses.ApiErrorResponse;

@RequestMapping("/admin/addresses")
@SecurityRequirement(name = "Bearer Authentication")
public interface ValidAddressController {

    @Operation(summary = "Add an acceptable address")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Address successfully added"),
            @ApiResponse(
                    responseCode = "409",
                    description = "Address already exists",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class))),
    })
    @PostMapping("/{addressName}")
    void addValidAddress(@PathVariable("addressName") String addressName);

    @Operation(summary = "Delete an acceptable address")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Address successfully added"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Address not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class))),
    })
    @DeleteMapping("/{addressName}")
    void deleteValidAddress(@PathVariable("addressName") String addressName);
}
