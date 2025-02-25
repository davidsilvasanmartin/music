package dev.davidsilva.musictests;

import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.Test;

import static dev.davidsilva.musictests.Login.givenLoggedInAsAdmin;
import static org.hamcrest.Matchers.*;

public class TestUsers extends TestSuite {
    @Test
    void deleteLastAdminUser() {
        givenLoggedInAsAdmin().contentType(ContentType.JSON).when().delete("users/admin").then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", containsStringIgnoringCase("cannot delete last admin user"));
    }

    @Test
    void getUserByUsername() {
        givenLoggedInAsAdmin().when().get("users/admin").then()
                .statusCode(HttpStatus.SC_OK).contentType(ContentType.JSON)
                .body("username", equalTo("admin"))
                .body(not(hasProperty("password")))
                .body("email", equalTo("admin@email.test"))
                .body("createdAt", is(not(emptyString())))
                .body("updatedAt", is(not(emptyString())))
                .body("enabled", equalTo(true))
                .body("roles.size()", equalTo(1))
                .body("roles[0].name", equalTo("ADMIN"))
                .body("roles[0].description", is(not(emptyString())))
                .body("roles[0].createdAt", is(not(emptyString())))
                .body("roles[0].updatedAt", is(not(emptyString())))
                .body("roles[0].permissions.size()", equalTo(5))
                // TODO maybe check permissions one by one
                .body("content[0].roles[0].permissions[0].name", is(not(emptyString())))
                .body("content[0].roles[0].permissions[0].description", is(not(emptyString())))
                .body("content[0].roles[0].permissions[0].createdAt", is(not(emptyString())))
                .body("content[0].roles[0].permissions[0].updatedAt", is(not(emptyString())));
    }

    @Test(dependsOnMethods = {"deleteCreatedAdminUser"})
    void getUsers() {
        givenLoggedInAsAdmin().when().get("users").then()
                .statusCode(HttpStatus.SC_OK).contentType(ContentType.JSON)
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

    @Test(dependsOnMethods = {"deleteLastAdminUser"})
    void createAdminUser() {
        JSONArray roles = new JSONArray();
        JSONObject role = new JSONObject();
        role.put("name", "ADMIN");
        roles.put(role);

        JSONObject user = new JSONObject();
        user.put("username", "test");
        user.put("email", "user@email.test");
        user.put("password", "test-passwd");
        user.put("roles", roles);
        user.put("enabled", true);

        givenLoggedInAsAdmin().contentType(ContentType.JSON).body(user.toString()).when().post("users").then()
                .statusCode(HttpStatus.SC_CREATED).contentType(ContentType.JSON)
                .body("username", equalTo("test"))
                .body(not(hasProperty("password")))
                .body("email", equalTo("user@email.test"))
                .body("createdAt", is(not(emptyString())))
                .body("updatedAt", is(not(emptyString())))
                .body("enabled", equalTo(true))
                .body("roles.size()", equalTo(1))
                .body("roles[0].name", equalTo("ADMIN"))
                .body("roles[0].description", is(not(emptyString())))
                .body("roles[0].createdAt", is(not(emptyString())))
                .body("roles[0].updatedAt", is(not(emptyString())))
                .body("roles[0].permissions.size()", equalTo(5));
    }

    @Test(dependsOnMethods = {"createAdminUser"})
    void deleteCreatedAdminUser() {
        givenLoggedInAsAdmin().contentType(ContentType.JSON).when().delete("users/test").then()
                .statusCode(HttpStatus.SC_NO_CONTENT);
    }

    @Test
    void getNonExistentUser() {
        givenLoggedInAsAdmin().when().get("users/NON_EXISTENT").then()
                .statusCode(HttpStatus.SC_NOT_FOUND).and().contentType(ContentType.JSON)
                .body("message", containsStringIgnoringCase("user with username NON_EXISTENT was not found"));
    }

    @Test
    void deleteNonExistentUser() {
        givenLoggedInAsAdmin().contentType(ContentType.JSON).when().delete("users/NON_EXISTENT").then()
                .statusCode(HttpStatus.SC_NOT_FOUND).and().contentType(ContentType.JSON)
                .body("message", containsStringIgnoringCase("user with username NON_EXISTENT was not found"));
    }
}
