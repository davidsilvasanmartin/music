import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;

public class TestAlbums {
    final int albumId = 1;

    @Test
    void getAlbums() {
        given().when().get("albums").then()
                .statusCode(200).contentType("application/json").body("number", equalTo(0))
                .body("size", equalTo(10)).body("content.size()", greaterThan(0));
    }

    @Test
    void getAlbumsWithPage() {
        given().when().get("albums?page=2").then()
                .statusCode(200).contentType("application/json").body("number", equalTo(2))
                .body("size", equalTo(10)).body("content.size()", greaterThan(0));
    }

    @Test
    void getAlbum() {
        given().when().get("albums/" + albumId).then()
                .statusCode(200).and().contentType("application/json");
    }

    @Test
    void getNonExistingAlbum() {
        given().when().get("albums/99999999").then()
                .statusCode(404).and().contentType("application/json");
    }
}
