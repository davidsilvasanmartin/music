package dev.davidsilva.musictests;

import org.apache.http.HttpStatus;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.hamcrest.Matchers.equalTo;

public class TestSongs {
    final int m4aSongId = 1;
    final String m4aSongTitle = "Song m4a";
    final String m4aSongLyrics = "";
    final int m4aSongAlbumId = 1;
    final String m4aSongAlbumArtist = "Flight of the Pink Goats";
    final String m4aSongAlbumTitle = "Album 1";
    final int flacSongId = 2;
    final int mp3SongId = 3;
    final int oggSongId = 4;
    final int nonExistentSongId = 99999999;

    @Test
    public void getSong() {
        given().when().get("songs/" + m4aSongId).then()
                .statusCode(200).and().contentType("application/json")
                .and().body("id", equalTo(m4aSongId))
                .and().body("album.id", equalTo(m4aSongAlbumId))
                .and().body("album.albumArtist", equalTo(m4aSongAlbumArtist))
                .and().body("album.album", equalTo(m4aSongAlbumTitle))
                .and().body("title", equalTo(m4aSongTitle))
                .and().body("lyrics", equalTo(m4aSongLyrics));
    }

    @Test
    public void getNonExistentSong() {
        given().when().get("songs/" + nonExistentSongId).then()
                .statusCode(HttpStatus.SC_NOT_FOUND).and().contentType("application/json")
                .body("message", containsStringIgnoringCase("song with id " + nonExistentSongId + " was not found"));
    }

    @Test
    public void getSongAlbumArt() {
        given().when().get("songs/" + m4aSongId + "/albumArt").then()
                .statusCode(200).and().contentType("application/json");
    }

    @Test
    public void getNonExistentSongAlbumArt() {
        given().when().get("songs/" + nonExistentSongId + "/albumArt").then()
                .statusCode(HttpStatus.SC_NOT_FOUND).and().contentType("application/json")
                .body("message", containsStringIgnoringCase("song with id " + nonExistentSongId + " was not found"));
    }

    @Test
    public void playM4aSong() {
        // Below is the Accept header that Firefox uses for <audio> elements
        given().accept("audio/*,*/*")
                .when().get("songs/" + m4aSongId + "/play").then()
                .statusCode(HttpStatus.SC_OK).and().contentType("audio/mp4");
    }

    @Test
    public void playFlacSong() {
        given().accept("audio/*,*/*")
                .when().get("songs/" + flacSongId + "/play").then()
                .statusCode(HttpStatus.SC_OK).and().contentType("audio/flac");
    }

    @Test
    public void playMp3Song() {
        given().accept("audio/*,*/*")
                .when().get("songs/" + mp3SongId + "/play").then()
                .statusCode(HttpStatus.SC_OK).and().contentType("audio/mpeg");
    }

    @Test
    public void playOggSong() {
        // Below is the Accept header that Firefox uses for <audio> elements
        given().accept("audio/*,*/*")
                .when().get("songs/" + oggSongId + "/play").then()
                .statusCode(HttpStatus.SC_OK).and().contentType("audio/ogg");
    }

    @Test
    public void playSongWithAcceptJson() {
        given().accept("application/json").when().get("songs/" + m4aSongId + "/play").then()
                .statusCode(HttpStatus.SC_NOT_ACCEPTABLE).and().contentType("application/json");
    }

    @Test
    public void playNonExistentSong() {
        given().when().get("songs/" + nonExistentSongId + "/play").then()
                .statusCode(HttpStatus.SC_NOT_FOUND).and().contentType("application/json")
                .body("message", containsStringIgnoringCase("song with id " + nonExistentSongId + " was not found"));
    }
}
