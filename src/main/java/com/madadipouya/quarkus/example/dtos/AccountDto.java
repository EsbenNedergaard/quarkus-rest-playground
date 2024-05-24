package com.madadipouya.quarkus.example.dtos;

import com.madadipouya.quarkus.example.entities.Account;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.validation.constraints.NotBlank;

@Schema(name = "AccountDTO", description = "Account DTO object")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {

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