package dev.davidsilva.musictests;

import org.apache.http.HttpStatus;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class TestAlbums {
    final int albumId = 1;
    final int nonExistentAlbumId = 99999999;
    final String albumArtist = "Dimitri From Paris";
    final String nonExistent = "#####NON_EXSTENT#####";
    // TODO fix application code to not break with commas
    final String albumBreakingTheSearch = "As Heard on Radio Soulwax, Part 2";
    final String album = "Cruising Attitude";
    final int albumYear = 2003;
    final String albumGenre = "House";

    @Test
    void getAlbums() {
        given().when().get("albums").then()
                .statusCode(HttpStatus.SC_OK).contentType("application/json").body("page", equalTo(1))
                .body("size", equalTo(10)).body("content.size()", greaterThan(0));
    }

    @Test
    void getAlbumsWithPage() {
        given().when().get("albums?page=2").then()
                .statusCode(HttpStatus.SC_OK).contentType("application/json").body("page", equalTo(2))
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
                .body("message", containsStringIgnoringCase("invalid search format:"));
    }

    @Test
    void searchAlbumsWithBadField() {
        given().when().get("albums?search=NON_EXISTENT_FIELD:eq:value").then()
                .statusCode(HttpStatus.SC_BAD_REQUEST).and().contentType("application/json")
                .body("message", containsStringIgnoringCase("invalid search:"));
    }

    @Test
    void searchAlbumsWithBadCondition() {
        given().when().get("albums?search=albumArtist:NON_EXISTENT_CONDITION:value").then()
                .statusCode(HttpStatus.SC_BAD_REQUEST).and().contentType("application/json")
                .body("message", containsStringIgnoringCase("invalid search operation:"));
    }

    @Test
    void searchAlbumsByArtist() {
        given().when().get("albums?search=albumArtist:eq:" + albumArtist).then()
                .statusCode(HttpStatus.SC_OK).and().contentType("application/json")
                .body("content.size()", greaterThan(0)).body("content[0].albumArtist", equalTo(albumArtist));
    }

    @Test
    void searchAlbumsByNonExistentAlbumArtist() {
        given().when().get("albums?search=albumArtist:eq:" + nonExistent).then()
                .statusCode(HttpStatus.SC_OK).and().contentType("application/json")
                .body("content.size()", equalTo(0));
    }

    @Test
    void searchAlbumsByAlbum() {
        given().when().get("albums?search=album:eq:" + album).then()
                .statusCode(HttpStatus.SC_OK).and().contentType("application/json")
                .body("content.size()", greaterThan(0)).body("content[0].album", equalTo(album));
    }

    @Test
    void searchAlbumsByAlbumThatIsBreakingTheSearch() {
        given().when().get("albums?search=album:eq:" + albumBreakingTheSearch).then()
                .statusCode(HttpStatus.SC_BAD_REQUEST).and().contentType("application/json")
                .body("message", containsStringIgnoringCase("invalid search format:"));
    }

    @Test
    void searchAlbumsByYear() {
        given().when().get("albums?search=year:eq:" + albumYear).then()
                .statusCode(HttpStatus.SC_OK).and().contentType("application/json")
                .body("content.size()", greaterThan(0)).body("content[0].year", equalTo(albumYear));
    }

    @Test
    void searchAlbumsByGenre() {
        given().when().get("albums?search=genre:contains:" + albumGenre).then()
                .statusCode(HttpStatus.SC_OK).and().contentType("application/json")
                .body("content.size()", greaterThan(0)).body("content[0].genres.toString()", containsString(albumGenre));
    }
}
