package com.esben.kaa.quarkus.example.controller;

import com.esben.kaa.quarkus.example.dtos.AccountDto;
import com.esben.kaa.quarkus.example.dtos.TransferRequestDto;
import com.esben.kaa.quarkus.example.entities.Account;
import com.esben.kaa.quarkus.example.exception.ResourceNotFoundException;
import com.esben.kaa.quarkus.example.exception.ValidationException;
import com.esben.kaa.quarkus.example.exceptionhandler.ExceptionHandler;
import com.esben.kaa.quarkus.example.service.AccountService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

@RequestScoped
@Path("/v1/accounts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@SecurityScheme(securitySchemeName = "Basic Auth", type = SecuritySchemeType.HTTP, scheme = "basic")
@Tag(name = "account", description = "Account operations.")
public class AccountController {

    private final AccountService accountService;

    @Inject
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GET
    @RolesAllowed({"USER", "ADMIN"})
    @Operation(summary = "Gets accounts", description = "Lists all available accounts")
    @APIResponses(value = @APIResponse(responseCode = "200", description = "Success",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Account.class))))
    public List<Account> getAccounts() {
        return accountService.getAllAccounts();
    }

    @GET
    @RolesAllowed({"USER", "ADMIN"})
    @Path("/{id}")
    @Operation(summary = "Gets an account", description = "Retrieves an account by id")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Account.class))),
            @APIResponse(responseCode = "404", description = "Account not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionHandler.ErrorResponseBody.class)))
    })
    public Response getAccount(@PathParam("id") int id) throws ResourceNotFoundException {
        return Response.ok(accountService.getAccountById(id)).build();
    }

    @POST
    @RolesAllowed({"ADMIN"})
    @Operation(summary = "Creates an account", description = "Creates an account and persists into database")
    @APIResponses(value = @APIResponse(responseCode = "201", description = "Created",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Account.class))))
    public Response createAccount(@Valid AccountDto accountDto) {
        Account newAccount = accountService.createNewAccount(accountDto.toAccount());
        return Response.status(Response.Status.CREATED).entity(newAccount).build();
    }

    @PUT
    @RolesAllowed("ADMIN")
    @Path("/{id}")
    @Operation(summary = "Updates a account", description = "Updates an existing account by id")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Account.class))),
            @APIResponse(responseCode = "404", description = "Account not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionHandler.ErrorResponseBody.class)))
    })
    public Response updateAccount(@PathParam("id") int id, @Valid AccountDto accountDto) throws ResourceNotFoundException {
        Account updatedAccount = accountService.updateAccount(id, accountDto.toAccount());
        return Response.ok(updatedAccount).build();
    }

    @DELETE
    @RolesAllowed("ADMIN")
    @Path("/{id}")
    @Operation(summary = "Deletes an account", description = "Deletes an account by id")
    @APIResponses(value = {
            @APIResponse(responseCode = "204", description = "Success"),
            @APIResponse(responseCode = "404", description = "Account not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionHandler.ErrorResponseBody.class)))
    })
    public Response deleteAccount(@PathParam("id") int id) throws ResourceNotFoundException {
        accountService.deleteAccount(id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @POST
    @RolesAllowed({"USER", "ADMIN"})
    @Path("/{id}/deposit")
    @Operation(summary = "Deposits money on an account", description = "Deposits the provided amount on the given account")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Account.class))),
            @APIResponse(responseCode = "404", description = "Account not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionHandler.ErrorResponseBody.class))),
            @APIResponse(responseCode = "400", description = "Bad request",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionHandler.ErrorResponseBody.class)))
    })
    public Response depositMoney(@PathParam("id") Long id,
                                 @Parameter(description = "Amount to deposit", required = true) @QueryParam("amount") int amount) throws ResourceNotFoundException, ValidationException {
        Account updatedAccount = accountService.deposit(id, amount);
        return Response.ok(updatedAccount).build();
    }

    @POST
    @RolesAllowed({"USER", "ADMIN"})
    @Path("/transfer")
    @Operation(summary = "Transfers money", description = "Transfer money from source to destination")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success"),
            @APIResponse(responseCode = "404", description = "Account not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionHandler.ErrorResponseBody.class))),
            @APIResponse(responseCode = "400", description = "Bad request",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionHandler.ErrorResponseBody.class)))
    })
    public Response transferMoney(TransferRequestDto transferRequest) throws ResourceNotFoundException, ValidationException {
        this.accountService.transferMoney(transferRequest);
        return Response.ok().build();
    }
}
