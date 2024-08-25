import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class TestSongs {
    static int songId = 1;

    @Test
    public void getSong() {
        given().when().get("songs/" + songId).then()
                .statusCode(200).and().contentType("application/json");
    }

    public void getNonExistingSong() {
        // TODO
    }

    public void getSongAlbumArt() {
        // TODO
    }

    public void getNonExistingSongAlbumArt() {
        // TODO
    }

    @Test
    public void playSongWithAcceptAudio() {
        given().accept("audio/webm,audio/ogg,audio/wav,audio/*;q=0.9,application/ogg;q=0.7,video/*;q=0.6,*/*;q=0.5")
                .when().get("songs/" + songId + "/play").then()
                .statusCode(200).and().contentType("audio/webm");
    }

    @Test
    public void playSongWithAcceptJson() {
        given().accept("application/json").when().get("songs/" + songId + "/play").then()
                .statusCode(406).and().contentType("application/json");
    }

    public void playNonExistingSong() {
        // TODO
    }
}
