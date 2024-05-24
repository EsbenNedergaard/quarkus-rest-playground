package com.esben.kaa.quarkus.example;

import com.esben.kaa.quarkus.example.dtos.AccountDto;
import com.esben.kaa.quarkus.example.dtos.TransferRequestDto;
import com.esben.kaa.quarkus.example.entities.Account;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

@QuarkusTest
public class AccountControllerTest {

    // TODO: hook up to some test database instead of them affecting the real database
    static Account testAccount;

    // We for now relly on the account setup in account.sql
    static String adminUserName = "admin";
    static String adminPassword = "admin";


    @BeforeEach
    public void beforeEach() {
        testAccount = createAccount("userName", "password", "USER");
    }


    @Test
    public void testCreateAccountEndpoint() {
        String userName = "userName";
        String password = "password";
        String role = "USER";
        String firstName = "firstName";
        String lastName = "lastName";

        AccountDto dto = new AccountDto(userName, password, role, firstName, lastName);
        Response response = given()
                .auth().basic(adminUserName, adminPassword)
                .body(dto)
                .contentType(ContentType.JSON)
                .when()
                .post("/v1/accounts")
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .extract()
                .response();

        Account account = response.as(Account.class);
        assertThat(account.getUsername(), equalTo(userName));
        assertThat(account.getPassword(), equalTo(null));  // Password is hidden in response
        assertThat(account.getRole(), equalTo(role));
        assertThat(account.getFirstName(), equalTo(firstName));
        assertThat(account.getLastName(), equalTo(lastName));
        assertThat(account.getBalanceInDkk(), equalTo(0)); // Starting balance is 0
    }

    @Test
    public void testListAccountsEndpoint() {
        given()
                .auth().basic(adminUserName, adminPassword)
                .when().get("/v1/accounts")
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void testGetAccountByIdEndpoint() {
        Response response = given()
                .auth().basic(adminUserName, adminPassword)
                .when().get("/v1/accounts/{id}", testAccount.getId())
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();

        Account account = response.as(Account.class);
        assertThat(account.getUsername(), equalTo(testAccount.getUsername()));
        assertThat(account.getRole(), equalTo(testAccount.getRole()));
        assertThat(account.getFirstName(), equalTo(testAccount.getFirstName()));
        assertThat(account.getLastName(), equalTo(testAccount.getLastName()));
        assertThat(account.getBalanceInDkk(), equalTo(testAccount.getBalanceInDkk()));
    }

    @Test
    public void testGetAccountByIdEndpoint_NotFound() {
        int unknownId = 10000000;
        given()
                .auth().basic(adminUserName, adminPassword)
                .when().get("/v1/accounts/{id}", unknownId)
                .then()
                .statusCode(HttpStatus.SC_NOT_FOUND);
    }


    @Test
    public void testUpdateAccountEndpoint() {
        AccountDto updateDto = new AccountDto("cannotUpdateUserName", "cannotUpdatePassword", "cannotUpdateRoles", "updatedFirstName", "updatedLastName");
        Response response = given()
                .auth().basic(adminUserName, adminPassword)
                .body(updateDto)
                .contentType(ContentType.JSON)
                .when()
                .put("/v1/accounts/{id}", testAccount.getId())
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();

        Account account = response.as(Account.class);

        assertThat(account.getFirstName(), equalTo(updateDto.getFirstName()));
        assertThat(account.getLastName(), equalTo(updateDto.getLastName()));

        // Some parts of the DTO will be ignored during UPDATE
        assertThat(account.getUsername(), not(equalTo(updateDto.getUsername())));
        assertThat(account.getPassword(), not(equalTo(updateDto.getPassword())));
        assertThat(account.getRole(), not(equalTo(updateDto.getRole())));
    }

    @Test
    public void testUpdateAccountEndpoint_NotFound() {
        int unknownId = 10000000;
        AccountDto updateDto = new AccountDto("userName", "password", "ADMIN", "updatedFirstName", "updatedLastName");
        given()
                .auth().basic(adminUserName, adminPassword)
                .body(updateDto)
                .contentType(ContentType.JSON)
                .when()
                .put("/v1/accounts/{id}", unknownId)
                .then()
                .statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    public void testDeleteAccountEndpoint() {
        given()
                .auth().basic(adminUserName, adminPassword)
                .when().delete("/v1/accounts/{id}", testAccount.getId())
                .then()
                .statusCode(HttpStatus.SC_NO_CONTENT);
    }

    @Test
    public void testDeleteAccountEndpoint_NotFound() {
        int unknownId = 10000000;

        given()
                .auth().basic(adminUserName, adminPassword)
                .when().delete("/v1/accounts/{id}", unknownId)
                .then()
                .statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    public void testDepositMoney() {
        Response response = given()
                .auth().basic(adminUserName, adminPassword)
                .contentType(ContentType.JSON)
                .queryParam("amount", 50)
                .when()
                .post("/v1/accounts/{id}/deposit", testAccount.getId())
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();

        Account account = response.as(Account.class);
        assertThat(account.getBalanceInDkk(), equalTo(50));
    }

    @Test
    public void testDepositMoney_Negative_Amount() {
        given()
                .auth().basic(adminUserName, adminPassword)
                .contentType(ContentType.JSON)
                .queryParam("amount", -1)
                .when()
                .post("/v1/accounts/{id}/deposit", testAccount.getId())
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    public void testTransferMoney() {
        Account sourceAccount = testAccount;
        Account destinationAccount = createAccount("user2", "password2", "USER");

        // Inserting money on source account
        int originalAmountOnSource = 50;
        given()
                .auth().basic(adminUserName, adminPassword)
                .contentType(ContentType.JSON)
                .queryParam("amount", 50)
                .when()
                .post("/v1/accounts/{id}/deposit", sourceAccount.getId())
                .then()
                .statusCode(HttpStatus.SC_OK);

        // Then transferring them
        int transferAmount = 40;
        TransferRequestDto transferRequestDto = new TransferRequestDto(sourceAccount.getId(), destinationAccount.getId(), transferAmount);
        given()
                .auth().basic(adminUserName, adminPassword)
                .contentType(ContentType.JSON)
                .body(transferRequestDto)
                .when()
                .post("/v1/accounts/transfer")
                .then()
                .statusCode(HttpStatus.SC_OK);

        // Afterward the source account has 10 DDK left
        Account fetchedSourceAccountAfterTransfer = fetchAccount(sourceAccount.getId());
        assertThat(fetchedSourceAccountAfterTransfer.getBalanceInDkk(), equalTo(10));

        // And destination has 40
        Account fetchedDestinationAccountAfterTransfer = fetchAccount(destinationAccount.getId());
        assertThat(fetchedDestinationAccountAfterTransfer.getBalanceInDkk(), equalTo(40));
    }

    @Test
    public void testTransferMoney_CannotTransferNegativeAmount() {
        Account sourceAccount = testAccount;
        Account destinationAccount = createAccount("user2", "password2", "USER");

        int transferAmount = -1;
        TransferRequestDto transferRequestDto = new TransferRequestDto(sourceAccount.getId(), destinationAccount.getId(), transferAmount);
        given()
                .auth().basic(adminUserName, adminPassword)
                .contentType(ContentType.JSON)
                .body(transferRequestDto)
                .when()
                .post("/v1/accounts/transfer")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    public void testTransferMoney_CannotTransferMoreThanIsOnSource() {
        Account sourceAccount = testAccount;
        Account destinationAccount = createAccount("user2", "password2", "USER");

        // Source account as 0 balance here.
        int transferAmount = 10;
        TransferRequestDto transferRequestDto = new TransferRequestDto(sourceAccount.getId(), destinationAccount.getId(), transferAmount);
        given()
                .auth().basic(adminUserName, adminPassword)
                .contentType(ContentType.JSON)
                .body(transferRequestDto)
                .when()
                .post("/v1/accounts/transfer")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }


    private static Account createAccount(String username, String password, String role) {
        AccountDto dto = new AccountDto(username, password, role, "firstName", "lastName");
        return given()
                .auth().basic(adminUserName, adminPassword)
                .body(dto)
                .contentType(ContentType.JSON)
                .when()
                .post("/v1/accounts")
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .extract()
                .as(Account.class);
    }

    private static Account fetchAccount(Long accountId) {
        return given()
                .auth().basic(adminUserName, adminPassword)
                .when().get("/v1/accounts/{id}", accountId)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response().as(Account.class);
    }

}