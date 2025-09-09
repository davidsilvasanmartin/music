package dev.davidsilva.music.job;

import dev.davidsilva.music.album.Album;
import dev.davidsilva.music.album.AlbumRepository;
import dev.davidsilva.music.artist.Artist;
import dev.davidsilva.music.artist.ArtistRepository;
import dev.davidsilva.music.beets.album.BeetsAlbum;
import dev.davidsilva.music.beets.album.BeetsAlbumRepository;
import dev.davidsilva.music.beets.item.BeetsItem;
import dev.davidsilva.music.genre.Genre;
import dev.davidsilva.music.genre.GenreRepository;
import dev.davidsilva.music.song.Song;
import dev.davidsilva.music.song.SongRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ImportAlbumsJobProcessorTest {

    @Mock
    private AlbumRepository albumRepository;
    // Other repositories and dependencies are not needed for the processor logic itself
    @Mock
    private GenreRepository genreRepository; // Not strictly needed but avoids NPE if used accidentally
    @Mock
    private ArtistRepository artistRepository; // Not strictly needed
    @Mock
    private SongRepository songRepository; // Not strictly needed
    @Mock
    private JobRepository jobRepository; // Not strictly needed
    @Mock
    private BeetsAlbumRepository beetsAlbumRepository; // Not strictly needed
    @Mock
    private PlatformTransactionManager appDbTransactionManager; // Not strictly needed

    private ItemProcessor<BeetsAlbum, Album> processor;

    @BeforeEach
    void setUp() {
        // Instantiate the class under test, injecting only the necessary mock for the processor
        // Use @InjectMocks OR manual instantiation. Manual is clearer here.
        ImportAlbumsJob importAlbumsJob = new ImportAlbumsJob(
                null, // not used by processor
                albumRepository,
                null,
                null,
                null,
                null,
                null
        );
        processor = importAlbumsJob.processor();
    }

    @Test
    void shouldProcessNewBeetsAlbumCorrectly() throws Exception {
        // --- Arrange ---
        // 1. Create BeetsItem (Input Song)
        BeetsItem beetsItem = new BeetsItem();
        beetsItem.setId(101);
        beetsItem.setMbTrackId("mb-track-id-1");
        beetsItem.setPath("/path/to/song.mp3");
        beetsItem.setTitle("Test Song Title");
        beetsItem.setTrackNumber(1);
        beetsItem.setTrackNumberTotal(10);
        beetsItem.setDiscNumber(1);
        beetsItem.setDiscNumberTotal(1);
        beetsItem.setLyrics("La la la");

        // 2. Create BeetsAlbum (Input Album)
        BeetsAlbum beetsAlbum = new BeetsAlbum();
        beetsAlbum.setId(1);
        beetsAlbum.setMbAlbumId("mb-album-id-1");
        beetsAlbum.setArtPath("/path/to/art.jpg");
        beetsAlbum.setAlbumArtist("Test Artist");
        beetsAlbum.setMbAlbumArtistId("mb-artist-id-1");
        beetsAlbum.setAlbum("Test Album Title");
        beetsAlbum.setGenre("Rock, Pop");
        beetsAlbum.setYear(2023);
        beetsAlbum.setSongs(Collections.singletonList(beetsItem));
        beetsItem.setAlbum(beetsAlbum); // Link back

        // 3. Configure Mock: Assume album does not exist yet
        when(albumRepository.existsByBeetsId(beetsAlbum.getId())).thenReturn(false);

        // --- Act ---
        Album processedAlbum = processor.process(beetsAlbum);

        // --- Assert ---
        assertNotNull(processedAlbum, "Processed album should not be null for a new album");

        // Verify Album details
        assertEquals(beetsAlbum.getId(), processedAlbum.getBeetsId());
        assertEquals(beetsAlbum.getMbAlbumId(), processedAlbum.getMbAlbumId());
        assertEquals(beetsAlbum.getArtPath(), processedAlbum.getArtPath());
        assertEquals(beetsAlbum.getAlbum(), processedAlbum.getAlbum());
        assertEquals(beetsAlbum.getYear(), processedAlbum.getYear());

        // Verify Artist details (nested inside Album)
        Artist processedArtist = processedAlbum.getArtist();
        assertNotNull(processedArtist, "Artist should not be null");
        assertEquals(beetsAlbum.getAlbumArtist(), processedArtist.getName());
        assertEquals(beetsAlbum.getMbAlbumArtistId(), processedArtist.getMbArtistId());

        // Verify Genre details (nested inside Album)
        Set<Genre> processedGenres = processedAlbum.getGenres();
        assertNotNull(processedGenres, "Genres should not be null");
        assertEquals(2, processedGenres.size());
        assertTrue(processedGenres.stream().anyMatch(g -> "Rock".equals(g.getName())));
        assertTrue(processedGenres.stream().anyMatch(g -> "Pop".equals(g.getName())));

        // Verify Song details (nested inside Album)
        List<Song> processedSongs = processedAlbum.getSongs();
        assertNotNull(processedSongs, "Songs should not be null");
        assertEquals(1, processedSongs.size());
        Song processedSong = processedSongs.getFirst();
        assertEquals(beetsItem.getId(), processedSong.getBeetsId());
        assertEquals(beetsItem.getMbTrackId(), processedSong.getMbTrackId());
        assertEquals(beetsItem.getPath(), processedSong.getPath());
        assertEquals(beetsItem.getTitle(), processedSong.getTitle());
        assertEquals(beetsItem.getTrackNumber(), processedSong.getTrackNumber());
        assertEquals(beetsItem.getTrackNumberTotal(), processedSong.getTrackNumberTotal());
        assertEquals(beetsItem.getDiscNumber(), processedSong.getDiscNumber());
        assertEquals(beetsItem.getDiscNumberTotal(), processedSong.getDiscNumberTotal());
        assertEquals(beetsItem.getLyrics(), processedSong.getLyrics());
        assertSame(processedAlbum, processedSong.getAlbum(), "Song should reference the parent processed album");

        // Verify mock interactions (optional but good practice)
        verify(albumRepository).existsByBeetsId(beetsAlbum.getId());
        verifyNoMoreInteractions(albumRepository); // Ensure no unexpected calls
    }

    @Test
    void shouldSkipExistingAlbum() throws Exception {
        // --- Arrange ---
        BeetsAlbum beetsAlbum = new BeetsAlbum();
        beetsAlbum.setId(2);
        beetsAlbum.setAlbum("Existing Album");
        beetsAlbum.setMbAlbumId("mb-album-id-2");
        // No need to set other fields as it should be skipped early

        // Configure Mock: Assume album *does* exist
        when(albumRepository.existsByBeetsId(beetsAlbum.getId())).thenReturn(true);

        // --- Act ---
        Album processedAlbum = processor.process(beetsAlbum);

        // --- Assert ---
        assertNull(processedAlbum, "Processed album should be null for an existing album");
        verify(albumRepository).existsByBeetsId(beetsAlbum.getId());
        verifyNoMoreInteractions(albumRepository);
    }
}