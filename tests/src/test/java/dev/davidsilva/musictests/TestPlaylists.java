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
 * TODO (mid-long term):
 *  - Create a song via API. Add that song to a playlist. Then, remove the song from the
 *      database altogether. Check that the id of the song in the playlist is set to null,
 *      but the cached properties of the playlist's entry are still present. For now, we
 *      can't add or delete songs, so we can't test this.
 *  - NOTE: the functionality explained above, or any other similar functionality that
 *      involves testing whether cached properties are kept when a song is deleted,
 *      is NOT testable yet
 */

/**
 * NOTE: these tests are meant to be run as one unit. Running them one by one will
 * cause failures, because the app's database state will be inconsistent with
 * what we are testing.
 */
public class TestPlaylists extends TestSuite {
    private static final String PLAYLISTS_ENDPOINT = "playlists";
    // Below are values that are used in multiple tests
    String playlistName = "Test Playlist";
    String playlistDescription = "Playlist created by automated tests";

    /**
     * Helper to create a basic playlist with specified song IDs and return its ID.
     * Assumes the creation is successful (status 201).
     */
    private int createPlaylistWithSongs(String name, String description, int... songIds) {
        JSONObject playlistJson = buildPlaylistJson(name, description, songIds);

        return givenLoggedInAsAdmin()
                .contentType(ContentType.JSON)
                .body(playlistJson.toString())
                .when()
                .post(PLAYLISTS_ENDPOINT)
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .extract()
                .path("id");
    }

    /**
     * Helper to build the JSON request body for creating/updating a playlist.
     * Sets dummy/ignored values for cached properties and position.
     */
    private JSONObject buildPlaylistJson(String name, String description, int... songIds) {
        JSONArray itemsJson = new JSONArray();
        for (int songId : songIds) {
            itemsJson.put(buildPlaylistItemJson(songId));
        }

        JSONObject playlistJson = new JSONObject();
        playlistJson.put("name", name);
        playlistJson.put("description", description);
        playlistJson.put("items", itemsJson);
        return playlistJson;
    }

    /**
     * Helper to build the JSON for a single playlist item.
     */
    private JSONObject buildPlaylistItemJson(int songId) {
        JSONObject songJson = new JSONObject();
        songJson.put("id", songId);

        JSONObject itemJson = new JSONObject();
        itemJson.put("song", songJson);
        // The API should ignore these values. It will instead extract them from
        // the corresponding Song/Album database objects.
        itemJson.put("mbTrackId", "_ignored_by_api_");
        itemJson.put("songTitle", "_ignored_by_api_");
        itemJson.put("mbAlbumId", "_ignored_by_api_");
        itemJson.put("albumTitle", "_ignored_by_api_");
        // Position is also ignored, what matters is the order in which the
        // songs are arranged in the playlist.
        itemJson.put("position", 99999);

        return itemJson;
    }

    @Test(priority = 1)
    void createPlaylistAndGetPlaylistById() {
        JSONObject playlistJson = buildPlaylistJson(playlistName, playlistDescription, 1, 2, 3, 4, 5);

        int playlistId = givenLoggedInAsAdmin()
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
                //  Do check other tests if we add them
                // .body("createdAt", is(notNullValue()))
                // .body("updatedAt", is(notNullValue()))
                .body("items", hasSize(5))
                // Verify positions are correctly set (0-based) and cached properties are fetched
                // Song at 0
                .body("items[0].position", equalTo(0))
                .body("items[0].song.id", equalTo(1))
                .body("items[0].mbTrackId", equalTo("mbtid1111"))
                .body("items[0].songTitle", equalTo("Song m4a"))
                .body("items[0].mbAlbumId", equalTo("mbaid1111"))
                .body("items[0].albumTitle", equalTo("Album 1"))
                // Song at 1
                .body("items[1].position", equalTo(1))
                .body("items[1].song.id", equalTo(2))
                .body("items[1].mbTrackId", equalTo("mbtid2222"))
                .body("items[1].songTitle", equalTo("Song flac"))
                .body("items[1].mbAlbumId", equalTo("mbaid1111"))
                .body("items[1].albumTitle", equalTo("Album 1"))
                // Song at 2
                .body("items[2].position", equalTo(2))
                .body("items[2].song.id", equalTo(3))
                .body("items[2].mbTrackId", equalTo("mbtid3333"))
                .body("items[2].songTitle", equalTo("Song mp3"))
                .body("items[2].mbAlbumId", equalTo("mbaid1111"))
                .body("items[2].albumTitle", equalTo("Album 1"))
                // Song at 3
                .body("items[3].position", equalTo(3))
                .body("items[3].song.id", equalTo(4))
                .body("items[3].mbTrackId", equalTo("mbtid4444"))
                .body("items[3].songTitle", equalTo("Song ogg"))
                .body("items[3].mbAlbumId", equalTo("mbaid1111"))
                .body("items[3].albumTitle", equalTo("Album 1"))
                // Song at 4
                .body("items[4].position", equalTo(4))
                .body("items[4].song.id", equalTo(5))
                .body("items[4].mbTrackId", equalTo("mbtid5555"))
                .body("items[4].songTitle", equalTo("Song for album without AlbumArt"))
                .body("items[4].mbAlbumId", equalTo("mbaid10101010"))
                .body("items[4].albumTitle", equalTo("Album 10 NoAlbumArt"))
                .extract()
                .path("id");

        // Testing the GET for the same playlist
        givenLoggedInAsAdmin()
                .contentType(ContentType.JSON)
                .body(playlistJson.toString())
                .when()
                .get(PLAYLISTS_ENDPOINT + "/" + playlistId)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .contentType(ContentType.JSON)
                .body("id", is(notNullValue()))
                .body("id", instanceOf(Integer.class))
                .body("name", equalTo(playlistName))
                .body("description", equalTo(playlistDescription))
                .body("items", hasSize(5))
                // Song at 0
                .body("items[0].position", equalTo(0))
                .body("items[0].song.id", equalTo(1))
                .body("items[0].mbTrackId", equalTo("mbtid1111"))
                .body("items[0].songTitle", equalTo("Song m4a"))
                .body("items[0].mbAlbumId", equalTo("mbaid1111"))
                .body("items[0].albumTitle", equalTo("Album 1"))
                // Song at 1
                .body("items[1].position", equalTo(1))
                .body("items[1].song.id", equalTo(2))
                .body("items[1].mbTrackId", equalTo("mbtid2222"))
                .body("items[1].songTitle", equalTo("Song flac"))
                .body("items[1].mbAlbumId", equalTo("mbaid1111"))
                .body("items[1].albumTitle", equalTo("Album 1"))
                // Song 2
                .body("items[2].position", equalTo(2))
                .body("items[2].song.id", equalTo(3))
                .body("items[2].mbTrackId", equalTo("mbtid3333"))
                .body("items[2].songTitle", equalTo("Song mp3"))
                .body("items[2].mbAlbumId", equalTo("mbaid1111"))
                .body("items[2].albumTitle", equalTo("Album 1"))
                // Song 3
                .body("items[3].position", equalTo(3))
                .body("items[3].song.id", equalTo(4))
                .body("items[3].mbTrackId", equalTo("mbtid4444"))
                .body("items[3].songTitle", equalTo("Song ogg"))
                .body("items[3].mbAlbumId", equalTo("mbaid1111"))
                .body("items[3].albumTitle", equalTo("Album 1"))
                // Song 4
                .body("items[4].position", equalTo(4))
                .body("items[4].song.id", equalTo(5))
                .body("items[4].mbTrackId", equalTo("mbtid5555"))
                .body("items[4].songTitle", equalTo("Song for album without AlbumArt"))
                .body("items[4].mbAlbumId", equalTo("mbaid10101010"))
                .body("items[4].albumTitle", equalTo("Album 10 NoAlbumArt"));
    }

    @Test(priority = 2, dependsOnMethods = {"createPlaylistAndGetPlaylistById"})
    void getPlaylists() {
        givenLoggedInAsAdmin()
                .when()
                .get(PLAYLISTS_ENDPOINT)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .contentType(ContentType.JSON)
                // At this point, only the playlist created in the previous test should exist,
                // so we can test here the response.
                .body("$", hasSize(1))
                .body("[0].id", is(notNullValue()))
                .body("[0].id", instanceOf(Integer.class))
                .body("[0].name", equalTo(playlistName))
                .body("[0].description", equalTo(playlistDescription))
                .body("[0].items", hasSize(5))
                // Verify positions are correctly set (0-based) and cached properties are fetched
                // Song at 0
                .body("[0].items[0].position", equalTo(0))
                .body("[0].items[0].song.id", equalTo(1))
                .body("[0].items[0].mbTrackId", equalTo("mbtid1111"))
                .body("[0].items[0].songTitle", equalTo("Song m4a"))
                .body("[0].items[0].mbAlbumId", equalTo("mbaid1111"))
                .body("[0].items[0].albumTitle", equalTo("Album 1"))
                // Song at 1
                .body("[0].items[1].position", equalTo(1))
                .body("[0].items[1].song.id", equalTo(2))
                .body("[0].items[1].mbTrackId", equalTo("mbtid2222"))
                .body("[0].items[1].songTitle", equalTo("Song flac"))
                .body("[0].items[1].mbAlbumId", equalTo("mbaid1111"))
                .body("[0].items[1].albumTitle", equalTo("Album 1"))
                // Song at 2
                .body("[0].items[2].position", equalTo(2))
                .body("[0].items[2].song.id", equalTo(3))
                .body("[0].items[2].mbTrackId", equalTo("mbtid3333"))
                .body("[0].items[2].songTitle", equalTo("Song mp3"))
                .body("[0].items[2].mbAlbumId", equalTo("mbaid1111"))
                .body("[0].items[2].albumTitle", equalTo("Album 1"))
                // Song at 3
                .body("[0].items[3].position", equalTo(3))
                .body("[0].items[3].song.id", equalTo(4))
                .body("[0].items[3].mbTrackId", equalTo("mbtid4444"))
                .body("[0].items[3].songTitle", equalTo("Song ogg"))
                .body("[0].items[3].mbAlbumId", equalTo("mbaid1111"))
                .body("[0].items[3].albumTitle", equalTo("Album 1"))
                // Song at 4
                .body("[0].items[4].position", equalTo(4))
                .body("[0].items[4].song.id", equalTo(5))
                .body("[0].items[4].mbTrackId", equalTo("mbtid5555"))
                .body("[0].items[4].songTitle", equalTo("Song for album without AlbumArt"))
                .body("[0].items[4].mbAlbumId", equalTo("mbaid10101010"))
                .body("[0].items[4].albumTitle", equalTo("Album 10 NoAlbumArt"));
    }

    @Test(priority = 3)
    void updatePlaylistRemovePlaylistItem() {
        // 1. Create a playlist with 2 items (song 1, song 2)
        int playlistId = createPlaylistWithSongs("Remove item test", "Item removal", 1, 2);

        // 2. Prepare update JSON, keeping only song 2
        JSONObject playlistJson = buildPlaylistJson("Remove item test", "Item removal", 2);

        // 3. Send PUT request to update
        givenLoggedInAsAdmin()
                .contentType(ContentType.JSON)
                .body(playlistJson.toString())
                .when()
                .patch(PLAYLISTS_ENDPOINT + "/" + playlistId)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .contentType(ContentType.JSON)
                .body("id", equalTo(playlistId))
                .body("name", equalTo("Remove item test"))
                .body("description", equalTo("Item removal"))
                .body("items", hasSize(1))
                .body("items[0].position", equalTo(0))
                .body("items[0].song.id", equalTo(2))
                .body("items[0].mbTrackId", equalTo("mbtid2222"))
                .body("items[0].songTitle", equalTo("Song flac"))
                .body("items[0].mbAlbumId", equalTo("mbaid1111"))
                .body("items[0].albumTitle", equalTo("Album 1"));
    }

    @Test(priority = 3)
    void updatePlaylistAddRemoveSwap() {
        // Create a playlist with 3 items (songs 1, 2, 3)
        int playlistId = createPlaylistWithSongs("Update test", "Update ops", 1, 2, 3);
        // Original order: [1, 2, 3] -> Positions: [0, 1, 2]

        // 2. Prepare update JSON: remove song 2, add song 4, swap order to [4, 3, 1]
        JSONObject updatedPlaylistJson = buildPlaylistJson("Update test updated", "Update ops updated", 4, 3, 1);

        // 3. Send PUT request and verify
        givenLoggedInAsAdmin()
                .contentType(ContentType.JSON)
                .body(updatedPlaylistJson.toString())
                .when()
                .patch(PLAYLISTS_ENDPOINT + "/" + playlistId)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("id", equalTo(playlistId))
                .body("name", equalTo("Update test updated"))
                .body("description", equalTo("Update ops updated"))
                .body("items", hasSize(3))
                // Song at 0 (song 4)
                .body("items[0].position", equalTo(0))
                .body("items[0].song.id", equalTo(4))
                .body("items[0].mbTrackId", equalTo("mbtid4444"))
                .body("items[0].songTitle", equalTo("Song ogg"))
                .body("items[0].mbAlbumId", equalTo("mbaid1111"))
                .body("items[0].albumTitle", equalTo("Album 1"))
                // Song at 1 (song 3)
                .body("items[1].position", equalTo(1))
                .body("items[1].song.id", equalTo(3))
                .body("items[1].mbTrackId", equalTo("mbtid3333"))
                .body("items[1].songTitle", equalTo("Song mp3"))
                .body("items[1].mbAlbumId", equalTo("mbaid1111"))
                .body("items[1].albumTitle", equalTo("Album 1"))
                // Song at 2 (song 1)
                .body("items[2].position", equalTo(2))
                .body("items[2].song.id", equalTo(1))
                .body("items[2].mbTrackId", equalTo("mbtid1111"))
                .body("items[2].songTitle", equalTo("Song m4a"))
                .body("items[2].mbAlbumId", equalTo("mbaid1111"))
                .body("items[2].albumTitle", equalTo("Album 1"));
    }

    @Test(priority = 3)
    void updatePlaylistWithInvalidPositions() {
        // 1. Create a playlist with 2 items (song 1, 2)
        int playlistId = createPlaylistWithSongs("Invalid Pos Test", "Testing invalid positions", 1, 2);

        // 2. Prepare update JSON with the same songs but weird positions
        JSONArray itemsJson = new JSONArray();
        // Item for song 1 with pos 99
        JSONObject item1 = buildPlaylistItemJson(1);
        item1.put("position", 99);
        itemsJson.put(item1);
        // Item for song 2 with pos -5
        JSONObject item2 = buildPlaylistItemJson(2);
        item2.put("position", -5);
        itemsJson.put(item2);

        JSONObject playlistJson = new JSONObject();
        playlistJson.put("name", "Invalid Pos Test Updated");
        playlistJson.put("description", "Positions should be fixed");
        playlistJson.put("items", itemsJson);


        // 3. Send PUT request
        givenLoggedInAsAdmin()
                .contentType(ContentType.JSON)
                .body(playlistJson.toString())
                .when()
                .patch(PLAYLISTS_ENDPOINT + "/" + playlistId)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("id", equalTo(playlistId))
                .body("items", hasSize(2))
                // Verify positions were recalculated correctly based on array order
                .body("items[0].song.id", equalTo(1))
                .body("items[0].position", equalTo(0))
                .body("items[1].song.id", equalTo(2))
                .body("items[1].position", equalTo(1));
    }

    @Test(priority = 3)
    void updatePlaylistWithGarbageCachedProperties() {
        // 1. Create a playlist with 1 item (song 1)
        int playlistId = createPlaylistWithSongs("Garbage Cache Test", "Testing ignored cached props", 1);

        // 2. Prepare update JSON with song 1 but garbage cached properties
        // The helper function already puts garbage
        JSONObject itemJson = buildPlaylistItemJson(1);

        JSONArray itemsArray = new JSONArray();
        itemsArray.put(itemJson);

        JSONObject playlistJson = new JSONObject();
        playlistJson.put("name", "Garbage Cache Test Updated");
        playlistJson.put("description", "Cached props should be correct");
        playlistJson.put("items", itemsArray);

        // 3. Send PUT request
        givenLoggedInAsAdmin()
                .contentType(ContentType.JSON)
                .body(playlistJson.toString())
                .when()
                .patch(PLAYLISTS_ENDPOINT + "/" + playlistId)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("id", equalTo(playlistId))
                .body("items", hasSize(1))
                .body("items[0].position", equalTo(0))
                .body("items[0].song.id", equalTo(1))
                // Verify cached properties are the CORRECT ones, not the garbage sent
                .body("items[0].mbTrackId", equalTo("mbtid1111"))
                .body("items[0].songTitle", equalTo("Song m4a"))
                .body("items[0].mbAlbumId", equalTo("mbaid1111"))
                .body("items[0].albumTitle", equalTo("Album 1"));
    }

    @Test(priority = 3)
    void createPlaylistItemWithNonExistentSongId() {
        int nonExistentSongId = 99999;
        JSONObject playlistJson = buildPlaylistJson("Bad Song ID Test", "Should fail", nonExistentSongId);

        givenLoggedInAsAdmin()
                .contentType(ContentType.JSON)
                .body(playlistJson.toString())
                .when()
                .post(PLAYLISTS_ENDPOINT)
                .then()
                // Expecting 404 Not Found because the referenced Song doesn't exist
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .contentType(ContentType.JSON)
                .body("message", containsStringIgnoringCase("Song with id " + nonExistentSongId + " was not found"));
    }

    @Test(priority = 3)
    void createPlaylistItemWithoutSong() {
        JSONObject itemJson = new JSONObject();
        // Missing the "song" field entirely
        itemJson.put("position", 0);
        // Other fields don't matter here

        JSONArray itemsArray = new JSONArray();
        itemsArray.put(itemJson);

        JSONObject playlistJson = new JSONObject();
        playlistJson.put("name", "No Song Test");
        playlistJson.put("description", "Should fail validation");
        playlistJson.put("items", itemsArray);

        givenLoggedInAsAdmin()
                .contentType(ContentType.JSON)
                .body(playlistJson.toString())
                .when()
                .post(PLAYLISTS_ENDPOINT)
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .contentType(ContentType.JSON)
                .body("message", containsStringIgnoringCase("playlist items must be associated with a song on creation"));
    }

    @Test(priority = 3)
    void updatePlaylistItemWithNonExistentSongId() {
        int playlistId = createPlaylistWithSongs("Bad Song ID Test", "Should fail", 1);

        int nonExistentSongId = 99999;
        JSONObject playlistJson = buildPlaylistJson("Bad Song ID Test", "Should fail", nonExistentSongId);

        givenLoggedInAsAdmin()
                .contentType(ContentType.JSON)
                .body(playlistJson.toString())
                .when()
                .patch(PLAYLISTS_ENDPOINT + "/" + playlistId)
                .then()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .contentType(ContentType.JSON)
                // It returns the following message because the app expects that PlaylistItems
                // without a song, or with a song that can't be found, already exist in the database.
                // There can be some extremely unlucky timing when we get this error: if user 1 is creating
                // a playlist with song with id 10, which is in his front-end because he downloaded the
                // available songs a few moments ago, but user 2 has deleted song 10 from the database
                // before user 1 has saved his playlist. We won't handle this case.
                .body("message", containsStringIgnoringCase("playlistitem with id null was not found"));
    }

    @Test(priority = 3)
    void updatePlaylistItemWithoutSong() {
        int playlistId = createPlaylistWithSongs("No Song Test", "Should fail validation", 1);

        JSONObject itemJson = new JSONObject();
        // Missing the "song" field entirely
        itemJson.put("position", 0);
        // Other fields don't matter here

        JSONArray itemsArray = new JSONArray();
        itemsArray.put(itemJson);

        JSONObject playlistJson = new JSONObject();
        playlistJson.put("name", "No Song Test");
        playlistJson.put("description", "Should fail validation");
        playlistJson.put("items", itemsArray);

        givenLoggedInAsAdmin()
                .contentType(ContentType.JSON)
                .body(playlistJson.toString())
                .when()
                .patch(PLAYLISTS_ENDPOINT + "/" + playlistId)
                .then()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .contentType(ContentType.JSON)
                .body("message", containsStringIgnoringCase("playlistitem with id null was not found"));
    }

    @Test(priority = 3)
    void updateNonExistentPlaylist() {
        int nonExistentPlaylistId = 99999;
        JSONObject playlistJson = buildPlaylistJson("Bad Playlist ID Test", "Should fail", 1);

        givenLoggedInAsAdmin()
                .contentType(ContentType.JSON)
                .body(playlistJson.toString())
                .when()
                .patch(PLAYLISTS_ENDPOINT + "/" + nonExistentPlaylistId)
                .then()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .contentType(ContentType.JSON)
                .body("message", containsStringIgnoringCase("playlist with id " + nonExistentPlaylistId + " was not found"));
    }


    /**
     * This test clears any playlists created during the test run.
     * <p>
     * It is important that this test has the highest priority number
     * (so it's executed last)
     */
//    @Test(priority = 10)
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
