package dev.davidsilva.music.job;

import dev.davidsilva.music.album.Album;
import dev.davidsilva.music.album.AlbumRepository;
import dev.davidsilva.music.artist.Artist;
import dev.davidsilva.music.artist.ArtistRepository;
import dev.davidsilva.music.beets.album.BeetsAlbum;
import dev.davidsilva.music.beets.album.BeetsAlbumRepository;
import dev.davidsilva.music.genre.Genre;
import dev.davidsilva.music.genre.GenreRepository;
import dev.davidsilva.music.song.Song;
import dev.davidsilva.music.song.SongRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ImportAlbumsJob {
    private final PlatformTransactionManager appDbTransactionManager;
    private final AlbumRepository albumRepository;
    private final GenreRepository genreRepository;
    private final ArtistRepository artistRepository;
    private final SongRepository songRepository;
    private final JobRepository jobRepository;
    private final BeetsAlbumRepository beetsAlbumRepository;

    public ImportAlbumsJob(
            @Qualifier("appDbTransactionManager") PlatformTransactionManager appDbTransactionManager,
            AlbumRepository albumRepository,
            GenreRepository genreRepository,
            ArtistRepository artistRepository,
            SongRepository songRepository,
            JobRepository jobRepository,
            BeetsAlbumRepository beetsAlbumRepository
    ) {
        this.appDbTransactionManager = appDbTransactionManager;
        this.albumRepository = albumRepository;
        this.genreRepository = genreRepository;
        this.artistRepository = artistRepository;
        this.songRepository = songRepository;
        this.jobRepository = jobRepository;
        this.beetsAlbumRepository = beetsAlbumRepository;
    }

    private RepositoryItemReader<BeetsAlbum> reader() {
        return new RepositoryItemReaderBuilder<BeetsAlbum>()
                .name("beetsAlbumReader")
                .repository(beetsAlbumRepository)
                .methodName("findAll")
                .pageSize(100)
                .sorts(Collections.singletonMap("id", Sort.Direction.ASC))
                .build();
    }

    private ItemProcessor<BeetsAlbum, Album> processor() {
        return beetsAlbum -> {
            if (albumRepository.existsByBeetsId(beetsAlbum.getId())) {
                // Return null to indicate that this item should be filtered out
                // and not passed to the writer
                return null;
            }

            Album album = new Album();
            album.setBeetsId(beetsAlbum.getId());
            album.setMbAlbumId(beetsAlbum.getMbAlbumId());
            album.setArtPath(beetsAlbum.getArtPath());
            album.setAlbum(beetsAlbum.getAlbum());
            album.setYear(beetsAlbum.getYear());

            // Artist
            Artist artist = null;
            String artistName = beetsAlbum.getAlbumArtist();
            String mbArtistId = beetsAlbum.getMbAlbumArtistId();
            if (isPresent(artistName) && isPresent(mbArtistId)) {
                artist = new Artist();
                artist.setName(artistName);
                artist.setMbArtistId(mbArtistId);
            }
            album.setArtist(artist);

            // Genres
            if (isPresent(beetsAlbum.getGenre())) {
                Set<Genre> genres = Arrays.stream(beetsAlbum.getGenre().split(","))
                        .map(String::trim)
                        .filter(name -> !name.isEmpty())
                        .map(genreName -> {
                            Genre g = new Genre();
                            g.setName(genreName);
                            return g;
                        })
                        .collect(Collectors.toSet());
                album.setGenres(genres);
            }

            // Songs
            if (beetsAlbum.getSongs() != null) {
                List<Song> songs = beetsAlbum.getSongs().stream()
                        .map(beetsItem -> {
                            Song song = new Song();
                            song.setBeetsId(beetsItem.getId());
                            song.setMbTrackId(beetsItem.getMbTrackId());
                            song.setPath(beetsItem.getPath());
                            song.setTitle(beetsItem.getTitle());
                            song.setTrackNumber(beetsItem.getTrackNumber());
                            song.setTrackNumberTotal(beetsItem.getTrackNumberTotal());
                            song.setDiscNumber(beetsItem.getDiscNumber());
                            song.setDiscNumberTotal(beetsItem.getDiscNumberTotal());
                            song.setLyrics(beetsItem.getLyrics());
                            song.setAlbum(album);
                            return song;
                        })
                        .collect(Collectors.toList());
                album.setSongs(songs);
            } else {
                log.warn("No songs found for album: {}", album.getAlbum());
            }


            return album;
        };
    }

    private ItemWriter<Album> writer() {
        return albums -> {
            for (Album album : albums) {
                TransactionTemplate transactionTemplate = new TransactionTemplate(appDbTransactionManager);
                transactionTemplate.execute(status -> {
                    // Artist
                    Artist albumArtist = album.getArtist();
                    if (albumArtist != null) {
                        String albumArtistName = albumArtist.getName();
                        // Sometimes an artist with a particular MB artist id has different names on different releases.
                        // This needs to be checked. TODO: have artists_alternative_names table
                        Optional<Artist> existingArtistOptional = artistRepository.findByMbArtistId(albumArtist.getMbArtistId());
                        if (existingArtistOptional.isPresent()) {
                            Artist existingArtist = existingArtistOptional.get();
                            if (!albumArtistName.equals(existingArtist.getName())) {
                                log.warn("Artist name mismatch. DB name: \"{}\". New name: \"{}\"", existingArtist.getName(), albumArtistName);
                            }
                            album.setArtist(existingArtist);
                        } else {
                            album.setArtist(artistRepository.save(albumArtist));
                        }
                    }

                    // Genres
                    Set<Genre> processedGenres = new HashSet<>();
                    if (album.getGenres() != null) {
                        for (Genre genre : album.getGenres()) {
                            Optional<Genre> existingGenreOptional = genreRepository.findByNameIgnoreCase(genre.getName());
                            if (existingGenreOptional.isPresent()) {
                                processedGenres.add(existingGenreOptional.get());
                            } else {
                                processedGenres.add(genreRepository.save(genre));
                            }
                        }
                        album.setGenres(processedGenres);
                    }

                    // Save the album and get the id (needed for songs)
                    Album savedAlbum = albumRepository.save(album);

                    // Songs
                    if (savedAlbum.getSongs() != null) {
                        for (Song song : savedAlbum.getSongs()) {
                            // The album is already set on the song in the processor
                            // But we need to make sure it's the saved album instance with the ID
                            song.setAlbum(savedAlbum);
                            songRepository.save(song);
                        }
                    }

                    return savedAlbum;
                });
            }
        };
    }

    private Step step() {
        return new StepBuilder("printAlbumIdsStep", jobRepository)
                .<BeetsAlbum, Album>chunk(10, appDbTransactionManager)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

    public Job getImportAlbumsJob() {
        return new JobBuilder("importAlbumsJob", jobRepository)
                .start(step())
                .build();
    }

    private boolean isPresent(String str) {
        return str != null && !str.isEmpty();
    }
}