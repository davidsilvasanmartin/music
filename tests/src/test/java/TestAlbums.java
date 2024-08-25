import org.apache.http.HttpStatus;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;

public class TestAlbums {
    final int albumId = 1;
    final int nonExistingAlbumId = 99999999;

    @Test
    void getAlbums() {
        given().when().get("albums").then()
                .statusCode(HttpStatus.SC_OK).contentType("application/json").body("number", equalTo(0))
                .body("size", equalTo(10)).body("content.size()", greaterThan(0));
    }

    @Test
    void getAlbumsWithPage() {
        given().when().get("albums?page=2").then()
                .statusCode(HttpStatus.SC_OK).contentType("application/json").body("number", equalTo(2))
                .body("size", equalTo(10)).body("content.size()", greaterThan(0));
    }

    @Test
    void getAlbum() {
        given().when().get("albums/" + albumId).then()
                .statusCode(HttpStatus.SC_OK).and().contentType("application/json");
    }

    @Test
    void getNonExistingAlbum() {
        given().when().get("albums/" + nonExistingAlbumId).then()
                .statusCode(HttpStatus.SC_NOT_FOUND).and().contentType("application/json");
    }
}
