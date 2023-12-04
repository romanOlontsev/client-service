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
import ru.neoflex.users.models.requests.AccountRequest;
import ru.neoflex.users.models.responses.AccountResponse;
import ru.neoflex.users.models.responses.ApiErrorResponse;

import java.util.List;

@RestController
@RequestMapping("/api/account")
@Validated
public interface ClientController {

    @Operation(summary = "Create account based on header")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "The account was created successfully"
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
                    description = "Header not supported",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiErrorResponse.class)
                    )
            )
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    void accountCreation(
            @Parameter(in = ParameterIn.HEADER,
                    required = true)
            @RequestHeader(value = "x-Source")
            String header,
            @Parameter(in = ParameterIn.DEFAULT,
                    schema = @Schema(implementation = AccountRequest.class))
            @Valid @RequestBody
            AccountRequest request);

    @Operation(summary = "Get account by id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "The account was received successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AccountResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Account not found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiErrorResponse.class)
                    )
            )
    })
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    AccountResponse getAccountById(
            @Parameter(in = ParameterIn.PATH,
                    required = true)
            @PathVariable(value = "id") Long accountId);

    @Operation(summary = "Get accounts by params")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Accounts successfully received",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(
                                    schema = @Schema(implementation = AccountResponse.class)
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Request without parameters",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiErrorResponse.class)
                    )
            )
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    List<AccountResponse> findAccountByAccountFields(
            @Parameter(in = ParameterIn.QUERY)
            @RequestParam(value = "lastname", required = false) String lastName,
            @Parameter(in = ParameterIn.QUERY)
            @RequestParam(value = "firstname", required = false) String firstName,
            @Parameter(in = ParameterIn.QUERY)
            @RequestParam(value = "middlename", required = false) String middleName,
            @Parameter(in = ParameterIn.QUERY)
            @RequestParam(value = "phonenumber", required = false) String phoneNumber,
            @Parameter(in = ParameterIn.QUERY)
            @RequestParam(value = "email", required = false) String email
                                                    );
}
