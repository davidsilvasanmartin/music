package dev.davidsilva.musictests;

import org.apache.http.HttpStatus;
import org.testng.annotations.Test;

import static dev.davidsilva.musictests.Login.givenLoggedInAsAdmin;
import static org.hamcrest.Matchers.*;

public class TestUsers extends TestSuite {
    @Test
    void getUsers() {
        givenLoggedInAsAdmin().when().get("users").then()
                .statusCode(HttpStatus.SC_OK).contentType("application/json")
                .body("page", equalTo(1))
                .body("size", equalTo(10))
                .body("totalElements", equalTo(1))
                .body("content.size()", equalTo(1))
                .body("content[0].username", equalTo("admin"))
                .body("content[0].email", equalTo("admin@email.test"))
                .body("content[0].createdAt", is(not(emptyString())))
                .body("content[0].updatedAt", is(not(emptyString())))
                .body("content[0].enabled", equalTo(true))
                .body("content[0].roles.size()", equalTo(1))
                .body("content[0].roles[0].name", equalTo("ADMIN"))
                .body("content[0].roles[0].description", is(not(emptyString())))
                .body("content[0].roles[0].createdAt", is(not(emptyString())))
                .body("content[0].roles[0].updatedAt", is(not(emptyString())))
                .body("content[0].roles[0].permissions.size()", equalTo(5))
                // TODO maybe check permissions one by one
                .body("content[0].roles[0].permissions[0].name", is(not(emptyString())))
                .body("content[0].roles[0].permissions[0].description", is(not(emptyString())))
                .body("content[0].roles[0].permissions[0].createdAt", is(not(emptyString())))
                .body("content[0].roles[0].permissions[0].updatedAt", is(not(emptyString())));
    }
}
