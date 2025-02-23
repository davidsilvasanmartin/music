package dev.davidsilva.musictests;

import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public final class Login {
    private final static String adminCredentials = """
            {
              "username": "admin",
              "password": "admin"
            }""";
    private static String token = null;

    private Login() {
    }

    public static void initialize() {
        if (token == null) {
            token = given()
                    .contentType("application/json")
                    .body(adminCredentials)
                    .when()
                    .post("/auth/login")
                    .jsonPath()
                    .get("token");
        }
    }

    public static RequestSpecification givenLoggedInAsAdmin() {
        if (token == null) {
            throw new IllegalStateException("Login must be initialized before using it");
        }
        return given().header("Authorization", "Bearer " + token);
    }
}
