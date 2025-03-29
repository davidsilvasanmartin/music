package dev.davidsilva.music.job;

import dev.davidsilva.music.album.Album;
import dev.davidsilva.music.album.AlbumRepository;
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

import java.util.Collections;

@Component
public class ImportAlbumsJob {
    private final PlatformTransactionManager transactionManager;
    private final AlbumRepository albumRepository;
    private final JobRepository jobRepository;

    public ImportAlbumsJob(
            AlbumRepository albumRepository,
            JobRepository jobRepository,
            @Qualifier("appDbTransactionManager") PlatformTransactionManager transactionManager
    ) {
        this.albumRepository = albumRepository;
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
    }

    private RepositoryItemReader<Album> reader() {
        return new RepositoryItemReaderBuilder<Album>()
                .name("printAlbumReader")
                .repository(albumRepository)
                .methodName("findAll")
                .pageSize(100)
                .sorts(Collections.singletonMap("id", Sort.Direction.ASC))
                .build();
    }

    private ItemProcessor<Album, Album> processor() {
        return album -> album; // Pass through
    }

    private ItemWriter<Album> writer() {
        return items -> {
            for (Album album : items) {
                System.out.println("Album ID: " + album.getId());
            }
        };
    }

    private Step step() {
        return new StepBuilder("printAlbumIdsStep", jobRepository)
                .<Album, Album>chunk(10, transactionManager)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

    public Job getImportAlbumsJob() {
        return new JobBuilder("printAlbumIdsJob", jobRepository)
                .start(step())
                .build();
    }
}