package dev.davidsilva.musictests;

import org.apache.http.HttpStatus;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsStringIgnoringCase;

/**
 * Logging in as admin is already tested in the Login class: if login stops working, all tests will fail.
 * That's why this file will be
 */
public class TestAuth extends TestSuite {
    private final static String authenticationFailedErrorMessage = "Authentication failed. Full authentication is required to access this resource";

    @Test
    void loginWithBadFormatCredentials() {
        given()
                .contentType("application/json")
                .body("{\"qqq\": \"www\"}")
                .when()
                .post("/auth/login")
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED).and().contentType("application/json")
                .body("message", containsStringIgnoringCase(authenticationFailedErrorMessage));
    }

    @Test
    void loginWithNonExistentUser() {
        given()
                .contentType("application/json")
                .body("{\"username\": \"NON_EXISTENT\", \"password\": \"NON_EXISTENT\"}")
                .when()
                .post("/auth/login")
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED).and().contentType("application/json")
                .body("message", containsStringIgnoringCase(authenticationFailedErrorMessage));
    }

    @Test
    void loginWithExistingUserButBadPassword() {
        given()
                .contentType("application/json")
                .body("{\"username\": \"admin\", \"password\": \"NON_EXISTENT\"}")
                .when()
                .post("/auth/login")
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED).and().contentType("application/json")
                .body("message", containsStringIgnoringCase(authenticationFailedErrorMessage));
    }

}
