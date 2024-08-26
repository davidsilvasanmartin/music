import org.apache.http.HttpStatus;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class TestAlbums {
    final int albumId = 1;
    final int nonExistentAlbumId = 99999999;
    final String albumArtist = "2Pac featuring Dr. Dre";
    final String nonExistentAlbumArtist = "#####NON_EXSTENT#####";

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
    void getAlbumById() {
        given().when().get("albums/" + albumId).then()
                .statusCode(HttpStatus.SC_OK).and().contentType("application/json");
    }

    @Test
    void getNonExistentAlbumById() {
        given().when().get("albums/" + nonExistentAlbumId).then()
                .statusCode(HttpStatus.SC_NOT_FOUND).and().contentType("application/json");
    }

    @Test
    void searchAlbumsWithBadFormat() {
        given().when().get("albums?search=qqq").then()
                .statusCode(HttpStatus.SC_BAD_REQUEST).and().contentType("application/json")
                .body("message", containsStringIgnoringCase("invalid search format"));
    }

    @Test
    void searchAlbumsWithBadField() {
        given().when().get("albums?search=NON_EXISTENT_FIELD:eq:value").then()
                .statusCode(HttpStatus.SC_BAD_REQUEST).and().contentType("application/json")
                .body("message", containsStringIgnoringCase("invalid search"));
    }

    @Test
    void searchAlbumsByArtist() {
        given().when().get("albums?search=albumArtist:eq:" + albumArtist).then()
                .statusCode(HttpStatus.SC_OK).and().contentType("application/json")
                .body("content.size()", greaterThan(0)).body("content[0].albumArtist", equalTo(albumArtist));
    }

    @Test
    void searchAlbumsByNonExistentAlbumArtist() {
        given().when().get("albums?search=albumArtist:eq:" + nonExistentAlbumArtist).then()
                .statusCode(HttpStatus.SC_OK).and().contentType("application/json")
                .body("content.size()", equalTo(0));
    }
}
