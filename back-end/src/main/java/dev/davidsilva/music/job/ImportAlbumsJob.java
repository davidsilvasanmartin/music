package dev.davidsilva.music.job;

import dev.davidsilva.music.album.Album;
import dev.davidsilva.music.album.AlbumRepository;
import dev.davidsilva.music.beets.album.BeetsAlbum;
import dev.davidsilva.music.beets.album.BeetsAlbumRepository;
import dev.davidsilva.music.genre.Genre;
import dev.davidsilva.music.genre.GenreRepository;
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
public class ImportAlbumsJob {
    private final PlatformTransactionManager transactionManager;
    private final AlbumRepository albumRepository;
    private final JobRepository jobRepository;
    private final BeetsAlbumRepository beetsAlbumRepository;
    private final GenreRepository genreRepository;

    public ImportAlbumsJob(
            AlbumRepository albumRepository,
            JobRepository jobRepository,
            @Qualifier("appDbTransactionManager") PlatformTransactionManager transactionManager,
            BeetsAlbumRepository beetsAlbumRepository, GenreRepository genreRepository
    ) {
        this.albumRepository = albumRepository;
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
        this.beetsAlbumRepository = beetsAlbumRepository;
        this.genreRepository = genreRepository;
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
            album.setAlbumArtist(beetsAlbum.getAlbumArtist());
            album.setAlbum(beetsAlbum.getAlbum());
            album.setYear(beetsAlbum.getYear());

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
                .<BeetsAlbum, Album>chunk(10, transactionManager)
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