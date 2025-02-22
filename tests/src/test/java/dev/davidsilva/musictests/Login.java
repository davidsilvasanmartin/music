package dev.davidsilva.musictests;

import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public final class Login {
    private final static String adminCredentials = """
            {
              "username": "admin",
              "password": "admin"
            }""";
    private static String token;

    private Login() {
    }

    public static void initialize() {
        token = given()
                .contentType("application/json")
                .body(adminCredentials)
                .when()
                .post("/auth/login")
                .jsonPath()
                .get("token");
    }

    public static RequestSpecification givenLoggedInAsAdmin() {
        return given().header("Authorization", "Bearer " + token);
    }
}
