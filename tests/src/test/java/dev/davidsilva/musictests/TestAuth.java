package dev.davidsilva.musictests;

import io.restassured.http.ContentType;
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
                .contentType(ContentType.JSON)
                .body("{\"qqq\": \"www\"}")
                .when()
                .post("/auth/login")
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED).and().contentType(ContentType.JSON)
                .body("message", containsStringIgnoringCase(authenticationFailedErrorMessage));
    }

    @Test
    void loginWithNonExistentUser() {
        given()
                .contentType(ContentType.JSON)
                .body("{\"username\": \"NON_EXISTENT\", \"password\": \"NON_EXISTENT\"}")
                .when()
                .post("/auth/login")
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED).and().contentType(ContentType.JSON)
                .body("message", containsStringIgnoringCase(authenticationFailedErrorMessage));
    }

    @Test
    void loginWithExistingUserButBadPassword() {
        given()
                .contentType(ContentType.JSON)
                .body("{\"username\": \"admin\", \"password\": \"NON_EXISTENT\"}")
                .when()
                .post("/auth/login")
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED).and().contentType(ContentType.JSON)
                .body("message", containsStringIgnoringCase(authenticationFailedErrorMessage));
    }

    @Test
    void getAlbumsWithoutAuthentication() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/albums")
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .contentType(ContentType.JSON)
                .body("message", containsStringIgnoringCase(authenticationFailedErrorMessage));
    }

    @Test
    void getAlbumsWithInvalidBearerToken() {
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer invalidRandomToken123")
                .when()
                .get("/albums")
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .contentType(ContentType.JSON)
                .body("message", containsStringIgnoringCase(authenticationFailedErrorMessage));
    }
}
