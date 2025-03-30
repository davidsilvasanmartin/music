package dev.davidsilva.music.job;

import dev.davidsilva.music.album.Album;
import dev.davidsilva.music.album.AlbumRepository;
import dev.davidsilva.music.artist.Artist;
import dev.davidsilva.music.artist.ArtistRepository;
import dev.davidsilva.music.beets.album.BeetsAlbum;
import dev.davidsilva.music.beets.album.BeetsAlbumRepository;
import dev.davidsilva.music.genre.Genre;
import dev.davidsilva.music.genre.GenreRepository;
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

import java.util.*;

@Component
@Slf4j
public class ImportAlbumsJob {
    private final PlatformTransactionManager appDbTransactionManager;
    private final AlbumRepository albumRepository;
    private final GenreRepository genreRepository;
    private final ArtistRepository artistRepository;
    private final JobRepository jobRepository;
    private final BeetsAlbumRepository beetsAlbumRepository;

    public ImportAlbumsJob(
            @Qualifier("appDbTransactionManager") PlatformTransactionManager appDbTransactionManager,
            AlbumRepository albumRepository,
            GenreRepository genreRepository,
            ArtistRepository artistRepository,
            JobRepository jobRepository,
            BeetsAlbumRepository beetsAlbumRepository
    ) {
        this.appDbTransactionManager = appDbTransactionManager;
        this.albumRepository = albumRepository;
        this.genreRepository = genreRepository;
        this.artistRepository = artistRepository;
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
            // TODO songs
            album.setBeetsId(beetsAlbum.getId());
            album.setMbAlbumId(beetsAlbum.getMbAlbumId());
            album.setArtPath(beetsAlbum.getArtPath());
            album.setAlbum(beetsAlbum.getAlbum());
            album.setYear(beetsAlbum.getYear());

            // Artist
            String artistName = beetsAlbum.getAlbumArtist();
            String mbArtistId = beetsAlbum.getMbAlbumArtistId();
            Artist artist;
            // Sometimes an artist with a particular MB artist id has different names on different releases.
            // This needs to be checked.
            Optional<Artist> existingArtist = artistRepository.findByMbArtistId(mbArtistId);
            if (existingArtist.isPresent()) {
                artist = existingArtist.get();
                if (!artistName.equals(artist.getName())) {
                    log.warn("Artist name mismatch. DB name: \"{}\". New name: \"{}\"", artist.getName(), artistName);
                }
            } else {
                artist = new Artist();
                artist.setName(artistName);
                artist.setMbArtistId(mbArtistId);
                artist = artistRepository.save(artist); // Save and get ID
            }
            album.setArtist(artist);


            // Genre
            if (beetsAlbum.getGenre() != null && !beetsAlbum.getGenre().isEmpty()) {
                List<String> genreNames = Arrays.stream(beetsAlbum.getGenre().split(","))
                        .map(String::trim)
                        .filter(name -> !name.isEmpty())
                        .toList();
                Set<Genre> genres = new HashSet<>();

                for (String genreName : genreNames) {
                    Genre genre = genreRepository.findByNameIgnoreCase(genreName)
                            .orElseGet(() -> {
                                Genre newGenre = new Genre();
                                newGenre.setName(genreName);
                                return genreRepository.save(newGenre);
                            });
                    genres.add(genre);
                }
                album.setGenres(genres);
            }

            return album;
        };
    }

    private ItemWriter<Album> writer() {
        return albumRepository::saveAll;
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
}