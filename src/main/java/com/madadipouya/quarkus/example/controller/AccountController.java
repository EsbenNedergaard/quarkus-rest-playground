package com.madadipouya.quarkus.example.controller;

import com.madadipouya.quarkus.example.exception.ResourceNotFoundException;
import com.madadipouya.quarkus.example.exceptionhandler.ExceptionHandler;
import com.madadipouya.quarkus.example.models.Account;
import com.madadipouya.quarkus.example.service.AccountService;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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
    public Account getAccount(@PathParam("id") int id) throws ResourceNotFoundException {
        return accountService.getAccountById(id);
    }

    @POST
    @RolesAllowed("ADMIN")
    @Operation(summary = "Creates an account", description = "Creates an account and persists into database")
    @APIResponses(value = @APIResponse(responseCode = "200", description = "Success",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Account.class))))
    public Account createAccount(@Valid AccountController.AccountDto accountDto) {
        return accountService.createNewAccount(accountDto.toAccount());
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
    public Account updateAccount(@PathParam("id") int id, @Valid AccountController.AccountDto accountDto) throws ResourceNotFoundException {
        return accountService.updateAccount(id, accountDto.toAccount());
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

    @Schema(name = "AccountDTO", description = "Account representation to create")
    @Getter
    @Setter
    public static class AccountDto {

        @NotBlank
        @Schema(title = "Username", required = true)
        private String username;

        @NotBlank
        @Schema(title = "Password", required = true)
        private String password;

        @Schema(title = "User role, either ADMIN or USER. USER is default")
        private String role;

        @NotBlank
        @Schema(title = "User first name", required = true)
        private String firstName;

        @NotBlank
        @Schema(title = "User lastname", required = true)
        private String lastName;

        public Account toAccount() {
            Account account = new Account();
            account.setUsername(username);
            account.setPassword(password);
            account.setRole(StringUtils.isBlank(role) ? "USER" : StringUtils.upperCase(role));
            account.setFirstName(firstName);
            account.setLastName(lastName);
            return account;
        }
    }
}
