package dev.davidsilva.musictests;

import org.apache.http.HttpStatus;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.hamcrest.Matchers.equalTo;

/**
 * This class tests all invalid searches.
 * We only test on one feature as we assume all of them are going to use the same search implementation
 */
public class TestSearch {
    final String nonExistent = "#####NON_EXSTENT#####";
    final String albumBreakingTheSearch = "whatever, with comma";

    @Test
    void searchAlbumsWithBadFormat() {
        given().when().get("albums?search=qqq").then()
                .statusCode(HttpStatus.SC_BAD_REQUEST).and().contentType("application/json")
                .body("message", containsStringIgnoringCase("invalid search format:"));
    }

    @Test
    void searchAlbumsWithBadField() {
        given().when().get("albums?search=" + nonExistent + ":eq:value").then()
                .statusCode(HttpStatus.SC_BAD_REQUEST).and().contentType("application/json")
                .body("message", containsStringIgnoringCase("invalid search:"));
    }

    @Test
    void searchAlbumsWithBadCondition() {
        given().when().get("albums?search=albumArtist:" + nonExistent + ":value").then()
                .statusCode(HttpStatus.SC_BAD_REQUEST).and().contentType("application/json")
                .body("message", containsStringIgnoringCase("invalid search operation:"));
    }

    @Test
    void searchAlbumsByNonExistentAlbumArtist() {
        given().when().get("albums?search=albumArtist:eq:" + nonExistent).then()
                .statusCode(HttpStatus.SC_OK).and().contentType("application/json")
                .body("content.size()", equalTo(0));
    }

    @Test
    void searchAlbumsByAlbumThatIsBreakingTheSearch() {
        given().when().get("albums?search=album:eq:" + albumBreakingTheSearch).then()
                .statusCode(HttpStatus.SC_BAD_REQUEST).and().contentType("application/json")
                .body("message", containsStringIgnoringCase("invalid search format:"));
    }
}
