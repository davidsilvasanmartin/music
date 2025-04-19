package dev.davidsilva.musictests;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import java.util.List;

import static dev.davidsilva.musictests.Login.givenLoggedInAsAdmin;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class TestPlaylists extends TestSuite {
    private static final String PLAYLISTS_ENDPOINT = "playlists";

    @Test(priority = 1)
    void createPlaylist() {
        String playlistName = "Test Playlist";
        String playlistDescription = "Playlist created by automated tests";
        int[] songIds = {1, 2, 3, 4, 5};

        JSONArray playlistItemsJson = new JSONArray();
        for (int songId : songIds) {
            JSONObject songJson = new JSONObject();
            songJson.put("id", songId);

            JSONObject playlistItemJson = new JSONObject();
            playlistItemJson.put("song", songJson);
            // Position to be the same as the songId
            playlistItemJson.put("position", songId);
            // Add empty strings for other fields as requested for the input DTO
            // TODO
            playlistItemJson.put("mbTrackId", "");
            playlistItemJson.put("songTitle", "");
            playlistItemJson.put("mbAlbumId", "");
            playlistItemJson.put("albumTitle", "");

            playlistItemsJson.put(playlistItemJson);
        }

        JSONObject playlistJson = new JSONObject();
        playlistJson.put("name", "Test Playlist");
        playlistJson.put("description", "Playlist created by automated tests");
        playlistJson.put("items", playlistItemsJson);

        givenLoggedInAsAdmin()
                .contentType(ContentType.JSON)
                .body(playlistJson.toString())
                .when()
                .post(PLAYLISTS_ENDPOINT)
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .contentType(ContentType.JSON)
                .body("id", is(notNullValue()))
                .body("id", instanceOf(Integer.class))
                .body("name", equalTo(playlistName))
                .body("description", equalTo(playlistDescription))
                // TODO unsure whether to return these
                // .body("createdAt", is(notNullValue()))
                // .body("updatedAt", is(notNullValue()))
                .body("items", hasSize(songIds.length))
                // TODO check cached properties on each song
                // TODO mbAlbumId not getting filled at the moment
                .body("items[0].song.id", equalTo(songIds[0]))
                .body("items[0].position", equalTo(0))
                .body("items[1].song.id", equalTo(songIds[1]))
                .body("items[1].position", equalTo(1))
                .body("items[2].song.id", equalTo(songIds[2]))
                .body("items[2].position", equalTo(2))
                .body("items[3].song.id", equalTo(songIds[3]))
                .body("items[3].position", equalTo(3))
                .body("items[4].song.id", equalTo(songIds[4]))
                .body("items[4].position", equalTo(4));
    }

    @Test(priority = 2)
    void getPlaylists() {
        // TODO complete checks
        givenLoggedInAsAdmin()
                .when()
                .get(PLAYLISTS_ENDPOINT)
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    /**
     * Clears any playlists created during the test run.
     * <p>
     * The alwaysRun=true makes sure cleanup runs even if some tests fail.
     */
    @AfterClass(alwaysRun = true)
    void cleanupPlaylists() {
        Response response = givenLoggedInAsAdmin()
                .when()
                .get(PLAYLISTS_ENDPOINT)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .contentType(ContentType.JSON)
                .extract().response();

        // The following line extracts the `id` field from each `PlaylistDto` object
        // within the JSON array response and collects them into a `List<Integer>`
        List<Integer> playlistIds = response.path("id");

        // Fail if NO playlists were found during cleanup.
        // This implies the create test might not have worked as expected.
        assertThat(playlistIds, is(not(empty())));

        for (int id : playlistIds) {
            givenLoggedInAsAdmin()
                    .when()
                    .delete(PLAYLISTS_ENDPOINT + "/" + id)
                    .then()
                    .statusCode(HttpStatus.SC_NO_CONTENT);
        }

        // Verify deletion by checking the database again
        givenLoggedInAsAdmin()
                .when()
                .get(PLAYLISTS_ENDPOINT)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .contentType(ContentType.JSON)
                .body("$", hasSize(0));
    }

}
