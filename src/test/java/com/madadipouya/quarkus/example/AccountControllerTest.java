package com.madadipouya.quarkus.example;

import com.madadipouya.quarkus.example.controller.AccountController;
import com.madadipouya.quarkus.example.entities.Account;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

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
        AccountController.AccountDto dto = new AccountController.AccountDto("userName", "password", "ADMIN", "firstName", "lastName");
        given()
                .auth().basic(adminUserName, adminPassword)
                .body(dto)
                .contentType(ContentType.JSON)
                .when()
                .post("/v1/accounts")
                .then()
                .statusCode(200);
    }

    @Test
    public void testListAccountsEndpoint() {
        given()
                .auth().basic(adminUserName, adminPassword)
                .when().get("/v1/accounts")
                .then()
                .statusCode(200);
    }

    @Test
    public void testGetAccountByIdEndpoint() {
        given()
                .auth().basic(adminUserName, adminPassword)
                .when().get("/v1/accounts/{id}", testAccount.getId())
                .then()
                .statusCode(200);
    }


    @Test
    public void testUpdateAccountEndpoint() {
        AccountController.AccountDto updateDto = new AccountController.AccountDto("updatedUserName", "updatedpassword", "ADMIN", "firstName", "lastName");

        testAccount = given()
                .auth().basic(adminUserName, adminPassword)
                .body(updateDto)
                .contentType(ContentType.JSON)
                .when()
                .put("/v1/accounts/{id}", testAccount.getId())
                .then()
                .statusCode(200)
                .extract()
                .as(Account.class);
    }

    @Test
    public void testDeleteAccountEndpoint() {
        given()
                .auth().basic(adminUserName, adminPassword)
                .when().delete("/v1/accounts/{id}", testAccount.getId())
                .then()
                .statusCode(204);
    }

    private static Account createAccount(String username, String password, String role) {
        AccountController.AccountDto dto = new AccountController.AccountDto(username, password, role, "firstName", "lastName");
        return given()
                .auth().basic(adminUserName, adminPassword)
                .body(dto)
                .contentType(ContentType.JSON)
                .when()
                .post("/v1/accounts")
                .then()
                .statusCode(200)
                .extract()
                .as(Account.class);
    }
}