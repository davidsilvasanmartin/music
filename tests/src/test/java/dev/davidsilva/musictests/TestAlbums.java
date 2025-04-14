package dev.davidsilva.musictests;

import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.testng.annotations.Test;

import static dev.davidsilva.musictests.Login.givenLoggedInAsAdmin;
import static org.hamcrest.Matchers.*;

// TODO update tests when changing signature for Genre

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
                .statusCode(HttpStatus.SC_OK).contentType(ContentType.JSON).body("page", equalTo(1))
                .body("size", equalTo(10)).body("content.size()", greaterThan(0));
    }

    @Test
    void getAlbumsWithPage() {
        givenLoggedInAsAdmin().when().get("albums?page=2").then()
                .statusCode(HttpStatus.SC_OK).contentType(ContentType.JSON).body("page", equalTo(2))
                .body("size", equalTo(10)).body("content.size()", greaterThan(0));
    }

    @Test
    void getAlbumsWithSize() {
        givenLoggedInAsAdmin().when().get("albums?size=20").then()
                .statusCode(HttpStatus.SC_OK).contentType(ContentType.JSON).body("page", equalTo(1))
                .body("size", equalTo(20)).body("content.size()", greaterThan(10));
    }

    @Test
    void sortAlbumsByArtistName() {
        givenLoggedInAsAdmin().when().get("albums?sort=artist.name,desc").then()
                .statusCode(HttpStatus.SC_OK).contentType(ContentType.JSON).body("page", equalTo(1))
                .body("size", equalTo(10)).body("content.size()", equalTo(10))
                .body("content[0].artist.name", equalTo("Mutant Blessed Birds"));
    }

    @Test
    void sortAlbumsByGenreName() {
        givenLoggedInAsAdmin().when().get("albums?sort=genres.name,desc&size=20").then()
                .statusCode(HttpStatus.SC_OK).and().contentType(ContentType.JSON)
                .body("content.size()", equalTo(11))
                // Testing here how albums having multiple genres are sorted
                .body("content[0].genres.toString()", containsString("MultiHouse 4"))
                .body("content[0].genres.toString()", containsString("MultiHouse 3"))
                .body("content[1].genres.toString()", containsString("MultiHouse 2"))
                .body("content[1].genres.toString()", containsString("MultiHouse 3"))
                .body("content[2].genres.toString()", containsString("House 9"))
                .body("content[3].genres.toString()", containsString("House 8"))
                .body("content[4].genres.toString()", containsString("House 7"))
                .body("content[5].genres.toString()", containsString("House 6"))
                .body("content[6].genres.toString()", containsString("House 5"))
                .body("content[7].genres.toString()", containsString("House 2"))
                .body("content[8].genres.toString()", containsString("House 10"))
                .body("content[9].genres.toString()", containsString("House 1"))
                .body("content[10].genres.toString()", containsString("Electropop"));
    }

    @Test
    void searchAlbumsBySongTitle() {
        givenLoggedInAsAdmin().when().get("albums?search=songs.title:eq:" + albumFirstSongTitle).then()
                .statusCode(HttpStatus.SC_OK).and().contentType(ContentType.JSON)
                .body("content.size()", equalTo(1))
                .body("content[0].songs[0].title", equalTo(albumFirstSongTitle));
    }

    @Test
    void getAlbumById() {
        givenLoggedInAsAdmin().when().get("albums/" + albumId).then()
                .statusCode(HttpStatus.SC_OK).contentType(ContentType.JSON)
                .body("id", equalTo(albumId))
                .body("artist.name", equalTo(albumArtist))
                .body("album", equalTo(album))
                .body("year", equalTo(albumYear))
                .body("genres.size()", equalTo(1))
                .body("genres", hasItem(albumGenre))
                .body("songs.size()", equalTo(albumNumberOfSongs))
                // Songs are sorted by discNumber and then by trackNumber. Have a look at the test Beets
                // database to understand the following sort.
                .body("songs[0].id", equalTo(albumFirstSongId))
                .body("songs[0].title", equalTo(albumFirstSongTitle))
                .body("songs[1].id", equalTo(4))
                .body("songs[1].title", equalTo("Song ogg"))
                .body("songs[2].id", equalTo(3))
                .body("songs[2].title", equalTo("Song mp3"))
                .body("songs[3].id", equalTo(2))
                .body("songs[3].title", equalTo("Song flac"));
    }

    @Test
    void getAlbumByNonExistentId() {
        givenLoggedInAsAdmin().when().get("albums/" + nonExistentAlbumId).then()
                .statusCode(HttpStatus.SC_NOT_FOUND).and().contentType(ContentType.JSON)
                .body("message", containsStringIgnoringCase("album with id " + nonExistentAlbumId + " was not found"));
    }

    @Test
    void searchAlbumsByArtistName() {
        givenLoggedInAsAdmin().when().get("albums?search=artist.name:eq:" + albumArtist).then()
                .statusCode(HttpStatus.SC_OK).and().contentType(ContentType.JSON)
                .body("content.size()", greaterThan(0)).body("content[0].artist.name", equalTo(albumArtist));
    }

    @Test
    void searchAlbumsByAlbum() {
        givenLoggedInAsAdmin().when().get("albums?search=album:eq:" + album).then()
                .statusCode(HttpStatus.SC_OK).and().contentType(ContentType.JSON)
                .body("content.size()", greaterThan(0)).body("content[0].album", equalTo(album));
    }

    @Test
    void searchAlbumsByYear() {
        givenLoggedInAsAdmin().when().get("albums?search=year:eq:" + albumYear).then()
                .statusCode(HttpStatus.SC_OK).and().contentType(ContentType.JSON)
                .body("content.size()", greaterThan(0)).body("content[0].year", equalTo(albumYear));
    }

    @Test
    void searchAlbumsByGenreNameContains() {
        givenLoggedInAsAdmin().when().get("albums?search=genres.name:contains:" + albumGenre).then()
                .statusCode(HttpStatus.SC_OK).and().contentType(ContentType.JSON)
                .body("content.size()", equalTo(2))
                .body("content[0].genres.toString()", containsString(albumGenre))
                // The album with "House 10" genre is also found because it contains "House 1"
                .body("content[1].genres.toString()", containsString("House 10"));
    }

    @Test
    void searchAlbumsByGenreNameContainsSortByArtistName() {
        givenLoggedInAsAdmin().when().get("albums?search=genres.name:contains:e&sort=artist.name,desc&size=20").then()
                .statusCode(HttpStatus.SC_OK).and().contentType(ContentType.JSON)
                .body("content.size()", equalTo(11))
                // Only check the first 2 albums, to verify the sort
                .body("content[0].artist.name", equalTo("Mutant Blessed Birds"))
                .body("content[1].artist.name", equalTo(albumArtist));
    }

    @Test
    void searchAlbumsByGenreNameContainsSortBySongTitle() {
        givenLoggedInAsAdmin().when().get("albums?search=genres.name:contains:e&sort=songs.title,asc&size=20").then()
                .statusCode(HttpStatus.SC_OK).and().contentType(ContentType.JSON)
                .body("content.size()", equalTo(11))
                // The first album is the only one that has songs...
                .body("content[0].album", equalTo(album))
                .body("content[0].songs.size()", equalTo(albumNumberOfSongs))
                // ... And its songs are sorted by title
                .body("content[0].songs[0].title", equalTo("Song flac"))
                .body("content[0].songs[1].title", equalTo("Song m4a"))
                .body("content[0].songs[2].title", equalTo("Song mp3"))
                .body("content[0].songs[3].title", equalTo("Song ogg"))
                // The rest of the albums, I'm not sure how they are sorted
                .body("content[1].album", equalTo("Album 2"));
    }

    @Test
    void searchAlbumsByArtistNameContainsSortByGenreName() {
        givenLoggedInAsAdmin().when().get("albums?search=artist.name:contains:e&sort=genres.name,asc&size=20").then()
                .statusCode(HttpStatus.SC_OK).and().contentType(ContentType.JSON)
                .body("content.size()", equalTo(11))
                .body("content[0].genres.toString()", containsString("Electropop"))
                .body("content[1].genres.toString()", containsString("House 1"));
    }

    @Test
    void searchAlbumsBySongTitleSortByArtistName() {
        givenLoggedInAsAdmin().when().get("albums?search=songs.title:eq:" + albumFirstSongTitle + "&sort=artist.name,desc").then()
                .statusCode(HttpStatus.SC_OK).and().contentType(ContentType.JSON)
                .body("content.size()", equalTo(1))
                .body("content[0].songs[0].title", equalTo(albumFirstSongTitle));
    }

    @Test
    void searchAlbumsBySongTitleSortByGenreName() {
        givenLoggedInAsAdmin().when().get("albums?search=songs.title:eq:" + albumFirstSongTitle + "&sort=genres.name,desc").then()
                .statusCode(HttpStatus.SC_OK).and().contentType(ContentType.JSON)
                .body("content.size()", equalTo(1))
                .body("content[0].songs[0].title", equalTo(albumFirstSongTitle));
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
                .statusCode(HttpStatus.SC_NOT_FOUND).and().contentType(ContentType.JSON)
                .body("message", containsStringIgnoringCase("album with id " + nonExistentAlbumId + " was not found"));
    }

    @Test
    public void getAlbumWithoutAlbumArtAlbumArt() {
        // TODO
    }
}
