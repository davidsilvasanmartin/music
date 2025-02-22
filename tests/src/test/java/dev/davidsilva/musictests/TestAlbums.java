package dev.davidsilva.musictests;

import org.apache.http.HttpStatus;
import org.testng.annotations.Test;

import static dev.davidsilva.musictests.Login.givenLoggedInAsAdmin;
import static org.hamcrest.Matchers.*;

public class TestAlbums extends TestSuite {
    final int albumId = 1;
    final int nonExistentAlbumId = 99999999;
    final String albumArtist = "Flight of the Pink Goats";
    final String album = "Album 1";
    final int albumYear = 2001;
    final String albumGenre = "House 1";
    final int albumNumberOfSongs = 4;
    final int albumFirstSongId = 1;
    final String albumFirstSongTitle = "Song m4a";

    @Test
    void getAlbums() {
        givenLoggedInAsAdmin().when().get("albums").then()
                .statusCode(HttpStatus.SC_OK).contentType("application/json").body("page", equalTo(1))
                .body("size", equalTo(10)).body("content.size()", greaterThan(0));
    }

    @Test
    void getAlbumsWithPage() {
        givenLoggedInAsAdmin().when().get("albums?page=2").then()
                .statusCode(HttpStatus.SC_OK).contentType("application/json").body("page", equalTo(2))
                .body("size", equalTo(10)).body("content.size()", greaterThan(0));
    }

    @Test
    void getAlbumsWithSize() {
        givenLoggedInAsAdmin().when().get("albums?size=20").then()
                .statusCode(HttpStatus.SC_OK).contentType("application/json").body("page", equalTo(1))
                .body("size", equalTo(20)).body("content.size()", greaterThan(10));
    }

    @Test
    void getAlbumsWithSort() {
        givenLoggedInAsAdmin().when().get("albums?sort=albumArtist,desc").then()
                .statusCode(HttpStatus.SC_OK).contentType("application/json").body("page", equalTo(1))
                .body("size", equalTo(10)).body("content.size()", equalTo(10))
                .body("content[0].albumArtist", equalTo("Mutant Blessed Birds"));
    }

    @Test
    void getAlbumById() {
        givenLoggedInAsAdmin().when().get("albums/" + albumId).then()
                .statusCode(HttpStatus.SC_OK).contentType("application/json")
                .body("id", equalTo(albumId))
                .body("albumArtist", equalTo(albumArtist))
                .body("album", equalTo(album))
                .body("year", equalTo(albumYear))
                .body("genres.size()", equalTo(1))
                .body("genres", hasItem(albumGenre))
                .body("songs.size()", equalTo(albumNumberOfSongs))
                .body("songs[0].id", equalTo(albumFirstSongId))
                .body("songs[0].title", equalTo(albumFirstSongTitle));
    }

    @Test
    void getNonExistentAlbumById() {
        givenLoggedInAsAdmin().when().get("albums/" + nonExistentAlbumId).then()
                .statusCode(HttpStatus.SC_NOT_FOUND).and().contentType("application/json")
                .body("message", containsStringIgnoringCase("album with id " + nonExistentAlbumId + " was not found"));
    }

    @Test
    void searchAlbumsByArtist() {
        givenLoggedInAsAdmin().when().get("albums?search=albumArtist:eq:" + albumArtist).then()
                .statusCode(HttpStatus.SC_OK).and().contentType("application/json")
                .body("content.size()", greaterThan(0)).body("content[0].albumArtist", equalTo(albumArtist));
    }

    @Test
    void searchAlbumsByAlbum() {
        givenLoggedInAsAdmin().when().get("albums?search=album:eq:" + album).then()
                .statusCode(HttpStatus.SC_OK).and().contentType("application/json")
                .body("content.size()", greaterThan(0)).body("content[0].album", equalTo(album));
    }

    @Test
    void searchAlbumsByYear() {
        givenLoggedInAsAdmin().when().get("albums?search=year:eq:" + albumYear).then()
                .statusCode(HttpStatus.SC_OK).and().contentType("application/json")
                .body("content.size()", greaterThan(0)).body("content[0].year", equalTo(albumYear));
    }

    @Test
    void searchAlbumsByGenre() {
        givenLoggedInAsAdmin().when().get("albums?search=genre:contains:" + albumGenre).then()
                .statusCode(HttpStatus.SC_OK).and().contentType("application/json")
                .body("content.size()", greaterThan(0)).body("content[0].genres.toString()", containsString(albumGenre));
    }

    @Test
    public void getAlbumAlbumArt() {
        givenLoggedInAsAdmin().accept("image/*,*/*").when().get("albums/" + albumId + "/albumArt").then()
                .statusCode(HttpStatus.SC_OK).and().contentType("image/png");
    }

    @Test
    public void getNonExistentAlbumAlbumArt() {
        // Please note: we need to add */* to the accepted types if we want the API to correctly return JSON
        // in case of error. Otherwise, the API will crash with a different (500) error. Browsers do
        // typically add */* to the list of content types when requesting files.
        givenLoggedInAsAdmin().accept("image/*,*/*").when().get("albums/" + nonExistentAlbumId + "/albumArt").then()
                .statusCode(HttpStatus.SC_NOT_FOUND).and().contentType("application/json")
                .body("message", containsStringIgnoringCase("album with id " + nonExistentAlbumId + " was not found"));
    }

    @Test
    public void getAlbumWithoutAlbumArtAlbumArt() {
        // todo
    }
}
