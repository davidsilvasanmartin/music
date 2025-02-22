package dev.davidsilva.musictests;

import io.restassured.RestAssured;
import org.testng.annotations.BeforeSuite;

public class TestSuite {
    public final static String baseUrl = "http://localhost:8080/api/";

    @BeforeSuite
    public void setup() {
        RestAssured.baseURI = baseUrl;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        Login.initialize();
    }
}
