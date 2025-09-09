//package dev.davidsilva.music.job;
//
//import dev.davidsilva.music.album.Album;
//import dev.davidsilva.music.album.AlbumRepository;
//import dev.davidsilva.music.artist.Artist;
//import dev.davidsilva.music.artist.ArtistRepository;
//import dev.davidsilva.music.beets.album.BeetsAlbumRepository;
//import dev.davidsilva.music.genre.Genre;
//import dev.davidsilva.music.genre.GenreRepository;
//import dev.davidsilva.music.song.Song;
//import dev.davidsilva.music.song.SongRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.ArgumentCaptor;
//import org.mockito.Captor;
//import org.mockito.Mock;
//import org.mockito.invocation.InvocationOnMock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.mockito.stubbing.Answer;
//import org.springframework.batch.core.repository.JobRepository;
//import org.springframework.batch.item.Chunk;
//import org.springframework.batch.item.ItemWriter;
//import org.springframework.transaction.PlatformTransactionManager;
//import org.springframework.transaction.TransactionDefinition;
//import org.springframework.transaction.TransactionStatus;
//import org.springframework.transaction.support.TransactionCallback;
//
//import java.util.HashSet;
//import java.util.List;
//import java.util.Optional;
//import java.util.Set;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertSame;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class ImportAlbumsJobWriterTest {
//
//    @Mock
//    private PlatformTransactionManager appDbTransactionManager;
//    @Mock
//    private AlbumRepository albumRepository;
//    @Mock
//    private GenreRepository genreRepository;
//    @Mock
//    private ArtistRepository artistRepository;
//    @Mock
//    private SongRepository songRepository;
//    // Other dependencies not directly used by writer logic
//    @Mock
//    private JobRepository jobRepository; // Not strictly needed
//    @Mock
//    private BeetsAlbumRepository beetsAlbumRepository; // Not strictly needed
//
//    @Mock
//    private TransactionStatus transactionStatus; // Mock TransactionStatus
//
//    // Captors to verify arguments passed to save methods
//    @Captor
//    private ArgumentCaptor<Artist> artistCaptor;
//    @Captor
//    private ArgumentCaptor<Genre> genreCaptor;
//    @Captor
//    private ArgumentCaptor<Album> albumCaptor;
//    @Captor
//    private ArgumentCaptor<Song> songCaptor;
//
//
//    private ImportAlbumsJob importAlbumsJob;
//    private ItemWriter<Album> writer;
//
//    @BeforeEach
//    void setUp() {
//        // Instantiate the class under test with all required mocks for the writer
//        importAlbumsJob = new ImportAlbumsJob(
//                appDbTransactionManager,
//                albumRepository,
//                genreRepository,
//                artistRepository,
//                songRepository,
//                null, // jobRepository - not used by writer
//                null  // beetsAlbumRepository - not used by writer
//        );
//        writer = importAlbumsJob.writer();
//
//        // --- Mock TransactionTemplate Execution ---
//        // This is crucial because the writer creates a new TransactionTemplate internally.
//        // We mock the transaction manager so that when the template calls transactionManager.execute,
//        // we can intercept the callback and execute it manually, allowing us to verify interactions within the transaction.
//        doAnswer(new Answer<Object>() {
//            @Override
//            public Object answer(InvocationOnMock invocation) throws Throwable {
//                TransactionCallback<?> callback = invocation.getArgument(0);
//                // Execute the transactional code immediately within the test context
//                return callback.doInTransaction(transactionStatus);
//            }
//        }).when(appDbTransactionManager).execute(any(TransactionDefinition.class), any(TransactionCallback.class));
//
//        // Simplified mocking for TransactionTemplate directly (Alternative, slightly less realistic)
//        doAnswer(invocation -> {
//            TransactionCallback<?> callback = invocation.getArgument(0);
//            return callback.doInTransaction(transactionStatus);
//        }).when(transactionTemplate).execute(any());
//    }
//
//    @Test
//    void shouldWriteNewAlbumArtistGenreAndSongCorrectly() throws Exception {
//        // --- Arrange ---
//        // 1. Create Artist (as would be created by processor)
//        Artist artist = new Artist();
//        artist.setName("Test Artist Write");
//        artist.setMbArtistId("mb-artist-id-write-1");
//
//        // 2. Create Genre (as would be created by processor)
//        Genre genre = new Genre();
//        genre.setName("Techno");
//        Set<Genre> genres = new HashSet<>();
//        genres.add(genre);
//
//        // 3. Create Song (as would be created by processor)
//        Song song = new Song();
//        song.setBeetsId(201);
//        song.setMbTrackId("mb-track-id-write-1");
//        song.setTitle("Test Song Write");
//        song.setPath("/path/write/song.mp3");
//        // ... set other song properties ...
//
//        // 4. Create Album (as would be created by processor)
//        Album album = new Album();
//        album.setBeetsId(10);
//        album.setMbAlbumId("mb-album-id-write-1");
//        album.setAlbum("Test Album Write");
//        album.setArtist(artist);
//        album.setGenres(genres);
//        album.setSongs(List.of(song));
//        // Link song back to album (processor does this)
//        song.setAlbum(album);
//
//        // 5. Prepare Album with ID (as returned by albumRepository.save)
//        Album savedAlbum = new Album();
//        // Copy properties or just set ID for simplicity in this test
//        savedAlbum.setId(50);
//        savedAlbum.setBeetsId(album.getBeetsId());
//        savedAlbum.setMbAlbumId(album.getMbAlbumId());
//        savedAlbum.setAlbum(album.getAlbum());
//        savedAlbum.setArtist(artist); // Assume artist is saved/fetched
//        savedAlbum.setGenres(genres); // Assume genres are saved/fetched
//        savedAlbum.setSongs(List.of(song)); // Crucial: keep the original song list
//
//
//        // 6. Configure Mocks:
//        // Assume Artist and Genre don't exist
//        when(artistRepository.findByMbArtistId(artist.getMbArtistId())).thenReturn(Optional.empty());
//        when(genreRepository.findByNameIgnoreCase(genre.getName())).thenReturn(Optional.empty());
//        // Mock save operations to return the saved entity (important for album ID)
//        when(artistRepository.save(any(Artist.class))).thenReturn(artist); // Return same artist for simplicity
//        when(genreRepository.save(any(Genre.class))).thenReturn(genre);   // Return same genre
//        when(albumRepository.save(any(Album.class))).thenReturn(savedAlbum); // Return the album *with ID*
//        when(songRepository.save(any(Song.class))).thenReturn(song);     // Return same song
//
//
//        // --- Act ---
//        writer.write(Chunk.of(album)); // Use Chunk or pass List directly depending on Batch version / usage
//
//        // --- Assert ---
//        // Verify lookups were performed
//        verify(artistRepository).findByMbArtistId(artist.getMbArtistId());
//        verify(genreRepository).findByNameIgnoreCase(genre.getName());
//
//        // Verify saves were performed
//        verify(artistRepository).save(artistCaptor.capture());
//        verify(genreRepository).save(genreCaptor.capture());
//        verify(albumRepository).save(albumCaptor.capture());
//        verify(songRepository).save(songCaptor.capture());
//
//        // Verify captured arguments
//        assertEquals("Test Artist Write", artistCaptor.getValue().getName());
//        assertEquals("Techno", genreCaptor.getValue().getName());
//        assertEquals("Test Album Write", albumCaptor.getValue().getAlbum());
//        // Check that the album passed to songRepository.save has the ID set
//        assertEquals("Test Song Write", songCaptor.getValue().getTitle());
//        assertSame(savedAlbum, songCaptor.getValue().getAlbum(), "Song should be associated with the *saved* album instance");
//        assertEquals(50, songCaptor.getValue().getAlbum().getId());
//
//
//        // Verify no more interactions (optional)
//        verifyNoMoreInteractions(artistRepository, genreRepository, albumRepository, songRepository);
//    }
//
//    @Test
//    void shouldWriteAlbumWithExistingArtistAndGenre() throws Exception {
//        // --- Arrange ---
//        // Existing entities in the "database" (mocks)
//        Artist existingArtist = new Artist();
//        existingArtist.setId(100);
//        existingArtist.setName("Existing Artist");
//        existingArtist.setMbArtistId("mb-artist-id-existing-1");
//
//        Genre existingGenre = new Genre();
//        existingGenre.setId(200);
//        existingGenre.setName("Existing Genre");
//
//        // Input entities (as from processor)
//        Artist inputArtist = new Artist();
//        inputArtist.setName("Existing Artist"); // Name might vary slightly in real data, but MBID is key
//        inputArtist.setMbArtistId("mb-artist-id-existing-1");
//
//        Genre inputGenre = new Genre();
//        inputGenre.setName("existing genre"); // Case-insensitive check
//
//        Set<Genre> inputGenres = new HashSet<>();
//        inputGenres.add(inputGenre);
//
//        Song song = new Song();
//        song.setBeetsId(301);
//        song.setTitle("Song With Existing Artist/Genre");
//
//        Album album = new Album();
//        album.setBeetsId(20);
//        album.setAlbum("Album With Existing");
//        album.setArtist(inputArtist); // Use the input one here
//        album.setGenres(inputGenres); // Use the input one here
//        album.setSongs(List.of(song));
//        song.setAlbum(album);
//
//        // Saved Album instance
//        Album savedAlbum = new Album();
//        savedAlbum.setId(60);
//        savedAlbum.setArtist(existingArtist); // Should be linked to existing
//        savedAlbum.setGenres(Set.of(existingGenre)); // Should be linked to existing
//        savedAlbum.setSongs(List.of(song));
//        savedAlbum.setAlbum(album.getAlbum());
//
//
//        // Configure Mocks: Artist and Genre *do* exist
//        when(artistRepository.findByMbArtistId(inputArtist.getMbArtistId())).thenReturn(Optional.of(existingArtist));
//        when(genreRepository.findByNameIgnoreCase(inputGenre.getName())).thenReturn(Optional.of(existingGenre));
//        when(albumRepository.save(any(Album.class))).thenReturn(savedAlbum); // Return saved album with ID
//        when(songRepository.save(any(Song.class))).thenReturn(song); // Return same song
//
//        // --- Act ---
//        writer.write(Chunk.of(album));
//
//        // --- Assert ---
//        // Verify lookups
//        verify(artistRepository).findByMbArtistId(inputArtist.getMbArtistId());
//        verify(genreRepository).findByNameIgnoreCase(inputGenre.getName());
//
//        // Verify NO saves for existing entities
//        verify(artistRepository, never()).save(any(Artist.class));
//        verify(genreRepository, never()).save(any(Genre.class));
//
//        // Verify Album and Song are saved
//        verify(albumRepository).save(albumCaptor.capture());
//        verify(songRepository).save(songCaptor.capture());
//
//        // Verify the album saved references the *existing* artist and genre
//        Album capturedAlbum = albumCaptor.getValue();
//        assertSame(existingArtist, capturedAlbum.getArtist());
//        assertEquals(1, capturedAlbum.getGenres().size());
//        assertSame(existingGenre, capturedAlbum.getGenres().iterator().next());
//
//        // Verify the song saved references the *saved* album (which references existing artist/genre)
//        Song capturedSong = songCaptor.getValue();
//        assertSame(savedAlbum, capturedSong.getAlbum());
//        assertEquals(60, capturedSong.getAlbum().getId());
//
//        verifyNoMoreInteractions(artistRepository); // Already verified findBy, never save
//        verifyNoMoreInteractions(genreRepository); // Already verified findBy, never save
//        verifyNoMoreInteractions(albumRepository, songRepository);
//    }
//}