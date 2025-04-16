package dev.davidsilva.musictests;

import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.testng.annotations.Test;

import static dev.davidsilva.musictests.Login.givenLoggedInAsAdmin;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

// TODO Finish TESTS

public class TestArtists extends TestSuite {
    final int artistId = 1;
    final int nonExistentArtistId = 99999999;
    final String artistName = "Flight of the Pink Goats";
    final String mbArtistId = "ff000708-c401-453a-a50a-ef8b1e10ff00";
    // Albums are sorted by year DESC
    final String artistFirstAlbumName = "Album 10 NoAlbumArt";
    final int artistAlbumYear = 2010;
    final String artistAlbumGenre = "House 10";
    final String artistFirstSongTitle = "Song for album without AlbumArt";

    @Test
    void getArtists() {
        givenLoggedInAsAdmin().when().get("artists").then()
                .statusCode(HttpStatus.SC_OK).contentType(ContentType.JSON)
                .body("page", equalTo(1))
                .body("size", equalTo(10))
                .body("totalElements", equalTo(2))
                .body("content.size()", equalTo(2))
                .body("content[0].name", equalTo(artistName))
                .body("content[0].mbArtistId", equalTo(mbArtistId))
                .body("content[0].albums.size()", equalTo(10))
                .body("content[0].albums[0].album", equalTo(artistFirstAlbumName))
                .body("content[0].albums[0].year", equalTo(artistAlbumYear))
                .body("content[0].albums[0].genres.size()", equalTo(1))
                .body("content[0].albums[0].genres.toString()", containsString(artistAlbumGenre))
                .body("content[0].albums[0].songs.size()", equalTo(1))
                .body("content[0].albums[0].songs[0].title", equalTo(artistFirstSongTitle));
    }

    @Test
    void getArtistsWithPage() {
        givenLoggedInAsAdmin().when().get("artists?page=2").then()
                .statusCode(HttpStatus.SC_OK).contentType(ContentType.JSON)
                .body("page", equalTo(2))
                .body("size", equalTo(10))
                .body("totalElements", equalTo(2))
                .body("content.size()", equalTo(0));
    }

    @Test
    void getArtistsWithSize() {
        givenLoggedInAsAdmin().when().get("artists?size=20").then()
                .statusCode(HttpStatus.SC_OK).contentType(ContentType.JSON)
                .body("page", equalTo(1))
                .body("size", equalTo(20))
                .body("totalElements", equalTo(2))
                .body("content.size()", equalTo(2));
    }

    @Test
    void sortArtistsByArtistName() {
        givenLoggedInAsAdmin().when().get("artists?sort=name,desc").then()
                .statusCode(HttpStatus.SC_OK).contentType(ContentType.JSON)
                .body("page", equalTo(1))
                .body("size", equalTo(10))
                .body("totalElements", equalTo(2))
                .body("content.size()", equalTo(2))
                .body("content[0].name", equalTo("Mutant Blessed Birds"));
    }

    @Test
    void sortArtistsByGenreName() {
        givenLoggedInAsAdmin().when().get("artists?sort=albums.genres.name,desc&size=20").then()
                .statusCode(HttpStatus.SC_OK).and().contentType(ContentType.JSON)
                .body("content.size()", equalTo(2))
                .body("content[0].name", equalTo(artistName))
                .body("content[0].albums.size()", equalTo(10))
                // The sort is IGNORED
                .body("content[0].albums[0].album", equalTo(artistFirstAlbumName))
                .body("content[0].albums[0].genres.toString()", containsString(artistAlbumGenre));
    }

//    @Test
//    void searchArtistsBySongTitle() {
//        givenLoggedInAsAdmin().when().get("artists?search=songs.title:eq:" + artistFirstSongTitle).then()
//                .statusCode(HttpStatus.SC_OK).and().contentType(ContentType.JSON)
//                .body("content.size()", equalTo(1))
//                .body("content[0].songs[0].title", equalTo(artistFirstSongTitle));
//    }

//    @Test
//    void getArtistById() {
//        givenLoggedInAsAdmin().when().get("artists/" + artistId).then()
//                .statusCode(HttpStatus.SC_OK).contentType(ContentType.JSON)
//                .body("id", equalTo(artistId))
//                .body("artist.name", equalTo(artistName))
//                .body("artist", equalTo(mbArtistId))
//                .body("year", equalTo(artistAlbumYear))
//                .body("genres.size()", equalTo(1))
//                .body("genres[0].name", equalTo(artistAlbumGenre))
//                .body("songs.size()", equalTo(artistNumberOfSongs))
//                // Songs are sorted by discNumber and then by trackNumber. Have a look at the test Beets
//                // database to understand the following sort.
//                .body("songs[0].id", equalTo(artistFirstSongId))
//                .body("songs[0].title", equalTo(artistFirstSongTitle))
//                .body("songs[1].id", equalTo(4))
//                .body("songs[1].title", equalTo("Song ogg"))
//                .body("songs[2].id", equalTo(3))
//                .body("songs[2].title", equalTo("Song mp3"))
//                .body("songs[3].id", equalTo(2))
//                .body("songs[3].title", equalTo("Song flac"));
//    }

//    @Test
//    void getArtistByNonExistentId() {
//        givenLoggedInAsAdmin().when().get("artists/" + nonExistentArtistId).then()
//                .statusCode(HttpStatus.SC_NOT_FOUND).and().contentType(ContentType.JSON)
//                .body("message", containsStringIgnoringCase("artist with id " + nonExistentArtistId + " was not found"));
//    }
//
//    @Test
//    void searchArtistsByArtistName() {
//        givenLoggedInAsAdmin().when().get("artists?search=artist.name:eq:" + artistName).then()
//                .statusCode(HttpStatus.SC_OK).and().contentType(ContentType.JSON)
//                .body("content.size()", greaterThan(0)).body("content[0].artist.name", equalTo(artistName));
//    }
//
//    @Test
//    void searchArtistsByArtist() {
//        givenLoggedInAsAdmin().when().get("artists?search=artist:eq:" + mbArtistId).then()
//                .statusCode(HttpStatus.SC_OK).and().contentType(ContentType.JSON)
//                .body("content.size()", greaterThan(0)).body("content[0].artist", equalTo(mbArtistId));
//    }
//
//    @Test
//    void searchArtistsByYear() {
//        givenLoggedInAsAdmin().when().get("artists?search=year:eq:" + artistAlbumYear).then()
//                .statusCode(HttpStatus.SC_OK).and().contentType(ContentType.JSON)
//                .body("content.size()", greaterThan(0)).body("content[0].year", equalTo(artistAlbumYear));
//    }
//
//    @Test
//    void searchArtistsByGenreNameContains() {
//        givenLoggedInAsAdmin().when().get("artists?search=genres.name:contains:" + artistAlbumGenre).then()
//                .statusCode(HttpStatus.SC_OK).and().contentType(ContentType.JSON)
//                .body("content.size()", equalTo(2))
//                .body("content[0].genres.toString()", containsString(artistAlbumGenre))
//                // The artist with "House 10" genre is also found because it contains "House 1"
//                .body("content[1].genres.toString()", containsString("House 10"));
//    }
//
//    @Test
//    void searchArtistsByGenreNameContainsSortByArtistName() {
//        givenLoggedInAsAdmin().when().get("artists?search=genres.name:contains:e&sort=artist.name,desc&size=20").then()
//                .statusCode(HttpStatus.SC_OK).and().contentType(ContentType.JSON)
//                .body("content.size()", equalTo(11))
//                // Only check the first 2 artists, to verify the sort
//                .body("content[0].artist.name", equalTo("Mutant Blessed Birds"))
//                .body("content[1].artist.name", equalTo(artistName));
//    }
//
//    @Test
//    void searchArtistsByGenreNameContainsSortBySongTitle() {
//        givenLoggedInAsAdmin().when().get("artists?search=genres.name:contains:e&sort=songs.title,asc&size=20").then()
//                .statusCode(HttpStatus.SC_OK).and().contentType(ContentType.JSON)
//                .body("content.size()", equalTo(11))
//                // The first artist has 4 songs...
//                .body("content[0].artist", equalTo(mbArtistId))
////                .body("content[0].songs.size()", equalTo(artistNumberOfSongs))
//                // ... And its songs are sorted by title
//                .body("content[0].songs[0].title", equalTo("Song flac"))
//                .body("content[0].songs[1].title", equalTo("Song m4a"))
//                .body("content[0].songs[2].title", equalTo("Song mp3"))
//                .body("content[0].songs[3].title", equalTo("Song ogg"))
//                // This other artist has a single song titled "Song for...", which gets sorted after "Song flac"
//                .body("content[1].artist", equalTo("Artist 10 NoArtistArt"))
//                // The rest of the artists, I'm not sure how they are sorted
//                .body("content[2].artist", equalTo("Artist 2"));
//    }
//
//    @Test
//    void searchArtistsByArtistNameContainsSortByGenreName() {
//        givenLoggedInAsAdmin().when().get("artists?search=artist.name:contains:e&sort=genres.name,asc&size=20").then()
//                .statusCode(HttpStatus.SC_OK).and().contentType(ContentType.JSON)
//                .body("content.size()", equalTo(11))
//                .body("content[0].genres.toString()", containsString("Electropop"))
//                .body("content[1].genres.toString()", containsString("House 1"));
//    }
//
//    @Test
//    void searchArtistsBySongTitleSortByArtistName() {
//        givenLoggedInAsAdmin().when().get("artists?search=songs.title:eq:" + artistFirstSongTitle + "&sort=artist.name,desc").then()
//                .statusCode(HttpStatus.SC_OK).and().contentType(ContentType.JSON)
//                .body("content.size()", equalTo(1))
//                .body("content[0].songs[0].title", equalTo(artistFirstSongTitle));
//    }
//
//    @Test
//    void searchArtistsBySongTitleSortByGenreName() {
//        givenLoggedInAsAdmin().when().get("artists?search=songs.title:eq:" + artistFirstSongTitle + "&sort=genres.name,desc").then()
//                .statusCode(HttpStatus.SC_OK).and().contentType(ContentType.JSON)
//                .body("content.size()", equalTo(1))
//                .body("content[0].songs[0].title", equalTo(artistFirstSongTitle));
//    }
//
//    @Test
//    public void getArtistArtistArt() {
//        givenLoggedInAsAdmin().accept("image/*,*/*").when().get("artists/" + artistId + "/artistArt").then()
//                .statusCode(HttpStatus.SC_OK).and().contentType("image/png");
//    }
//
//    @Test
//    public void getNonExistentArtistArtistArt() {
//        // Please note: we need to add */* to the accepted types if we want the API to correctly return JSON
//        // in case of error. Otherwise, the API will crash with a different (500) error. Browsers do
//        // typically add */* to the list of content types when requesting files.
//        givenLoggedInAsAdmin().accept("image/*,*/*").when().get("artists/" + nonExistentArtistId + "/artistArt").then()
//                .statusCode(HttpStatus.SC_NOT_FOUND).and().contentType(ContentType.JSON)
//                .body("message", containsStringIgnoringCase("artist with id " + nonExistentArtistId + " was not found"));
//    }
//
//    @Test
//    public void getArtistWithoutArtistArtArtistArt() {
//        givenLoggedInAsAdmin().accept("image/*,*/*").when().get("artists/10/artistArt").then()
//                .statusCode(HttpStatus.SC_NOT_FOUND).and().contentType(ContentType.JSON)
//                .body("message", containsStringIgnoringCase("artist art not found for artist with id 10"));
//    }
}
