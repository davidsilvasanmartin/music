import org.apache.http.HttpStatus;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class TestSongs {
    final int songId = 1;
    final int nonExistingSongId = 99999999;

    @Test
    public void getSong() {
        given().when().get("songs/" + songId).then()
                .statusCode(200).and().contentType("application/json");
    }

    @Test
    public void getNonExistingSong() {
        given().when().get("songs/" + nonExistingSongId).then()
                .statusCode(HttpStatus.SC_NOT_FOUND).and().contentType("application/json");
    }

    @Test
    public void getSongAlbumArt() {
        given().when().get("songs/" + songId + "/albumArt").then()
                .statusCode(200).and().contentType("application/json");
    }

    @Test
    public void getNonExistingSongAlbumArt() {
        given().when().get("songs/" + nonExistingSongId + "/albumArt").then()
                .statusCode(HttpStatus.SC_NOT_FOUND).and().contentType("application/json");
    }

    @Test
    public void playSongWithAcceptAudio() {
        // Below is the Accept header that Firefox uses for <audio> elements
        given().accept("audio/webm,audio/ogg,audio/wav,audio/*;q=0.9,application/ogg;q=0.7,video/*;q=0.6,*/*;q=0.5")
                .when().get("songs/" + songId + "/play").then()
                .statusCode(HttpStatus.SC_OK).and().contentType("audio/webm");
    }

    @Test
    public void playSongWithAcceptJson() {
        given().accept("application/json").when().get("songs/" + songId + "/play").then()
                .statusCode(HttpStatus.SC_NOT_ACCEPTABLE).and().contentType("application/json");
    }

    @Test
    public void playNonExistingSong() {
        given().when().get("songs/" + nonExistingSongId + "/play").then()
                .statusCode(HttpStatus.SC_NOT_FOUND).and().contentType("application/json");
    }
}
