package dev.davidsilva.musictests;

import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;

import static io.restassured.RestAssured.given;

public final class Login {
    private final static String adminCredentials = """
            {
              "username": "admin",
              "password": "admin"
            }""";
    private static final String token = null;
    private static String sessionCookie = null;


    private Login() {
    }

    public static void initialize() {
        if (sessionCookie == null) {
            sessionCookie = given()
                    .contentType(ContentType.JSON)
                    .body(adminCredentials).when()
                    .post("/auth/login")
                    .then()
                    .statusCode(HttpStatus.SC_OK)
                    .extract()
                    .response()
                    .getCookie("JSESSIONID");
            // Force importing Beets database. If this has been done before, nothing will happen,
            // because all albums will be ignored and not imported again.
            givenLoggedInAsAdmin().when().post("jobs/import-albums").then()
                    .statusCode(HttpStatus.SC_OK);
        }
    }

    public static RequestSpecification givenLoggedInAsAdmin() {
        if (sessionCookie == null) {
            throw new IllegalStateException("Login must be initialized before using it");
        }
        return given().cookie("JSESSIONID", sessionCookie);
    }
}
