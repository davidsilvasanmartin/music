package dev.davidsilva.musictests;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.Test;

import java.util.List;

import static dev.davidsilva.musictests.Login.givenLoggedInAsAdmin;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * TODO: TESTS TO ADD
 *  - Create a playlist with 2 items, remove one of them. Check the result.
 *  - Swap item positions in a playlist. (maybe unnecessary because it's included by the next one)
 *  - Update an existing playlist with all allowed operations: add a new song, remove an existing song, swap positions.
 *  - Update or create a playlist with invalid positions (positions should be ignored and recalculated from the array's order)
 *  - Update an item with garbage cached properties and check that they are not updated (this has already been tested when creating items)
 *  >>>> Items with no song id
 *  - Create a playlist item with a song with id that does not exist. (Expecting error "Song with id null does not exist")
 *  - Create a playlist item without song (Expecting error "PlaylistItem with id null does not exist")
 */

/**
 * TODO (mid-long term):
 *  - Create a song via API. Add that song to a playlist. Then, remove the song from the
 *      database altogether. Check that the id of the song in the playlist is set to null,
 *      but the cached properties of the playlist's entry are still present. For now, we
 *      can't add or delete songs, so we can't test this.
 *  - NOTE: the functionality explained above, or any other similar functionality that
 *      involves testing whether cached properties are kept when a song is deleted,
 *      is NOT testable yet
 */
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
            // The API should ignore these values. It will instead extract them from
            // the corresponding Song/Album database objects.
            playlistItemJson.put("mbTrackId", "_ignored_by_api_");
            playlistItemJson.put("songTitle", "_ignored_by_api_");
            playlistItemJson.put("mbAlbumId", "_ignored_by_api_");
            playlistItemJson.put("albumTitle", "_ignored_by_api_");
            // Position is also ignored, what matters is the order in which the
            // songs are arranged in the playlist.
            playlistItemJson.put("position", 99999);

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
                // Here we just test that the id is an integer.
                // If we have performed multiple test runs,
                // playlists would have been created with
                // increasing ids (and later, removed).
                // The next created playlist would have an id
                // of 1 greater than the previous one.
                .body("id", is(notNullValue()))
                .body("id", instanceOf(Integer.class))
                .body("name", equalTo(playlistName))
                .body("description", equalTo(playlistDescription))
                // TODO unsure whether to return these from the API or not (right now they are NOT returned)
                // .body("createdAt", is(notNullValue()))
                // .body("updatedAt", is(notNullValue()))
                .body("items", hasSize(songIds.length))
                // Song 0
                .body("items[0].song.id", equalTo(songIds[0]))
                .body("items[0].position", equalTo(0))
                .body("items[0].mbTrackId", equalTo("mbtid1111"))
                .body("items[0].songTitle", equalTo("Song m4a"))
                .body("items[0].mbAlbumId", equalTo("mbaid1111"))
                .body("items[0].albumTitle", equalTo("Album 1"))
                // Song 1
                .body("items[1].song.id", equalTo(songIds[1]))
                .body("items[1].position", equalTo(1))
                .body("items[1].mbTrackId", equalTo("mbtid2222"))
                .body("items[1].songTitle", equalTo("Song flac"))
                .body("items[1].mbAlbumId", equalTo("mbaid1111"))
                .body("items[1].albumTitle", equalTo("Album 1"))
                // Song 2
                .body("items[2].song.id", equalTo(songIds[2]))
                .body("items[2].position", equalTo(2))
                .body("items[2].mbTrackId", equalTo("mbtid3333"))
                .body("items[2].songTitle", equalTo("Song mp3"))
                .body("items[2].mbAlbumId", equalTo("mbaid1111"))
                .body("items[2].albumTitle", equalTo("Album 1"))
                // Song 3
                .body("items[3].song.id", equalTo(songIds[3]))
                .body("items[3].position", equalTo(3))
                .body("items[3].mbTrackId", equalTo("mbtid4444"))
                .body("items[3].songTitle", equalTo("Song ogg"))
                .body("items[3].mbAlbumId", equalTo("mbaid1111"))
                .body("items[3].albumTitle", equalTo("Album 1"))
                // Song 4
                .body("items[4].song.id", equalTo(songIds[4]))
                .body("items[4].position", equalTo(4))
                .body("items[4].mbTrackId", equalTo("mbtid5555"))
                .body("items[4].songTitle", equalTo("Song for album without AlbumArt"))
                .body("items[4].mbAlbumId", equalTo("mbaid10101010"))
                .body("items[4].albumTitle", equalTo("Album 10 NoAlbumArt"));
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
     * This test clears any playlists created during the test run.
     * <p>
     * It is important that this test has the highest priority number
     * (so it's executed last)
     */
    @Test(priority = 3)
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
        // This implies the createPlaylist test might not have worked as expected.
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
