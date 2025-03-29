package dev.davidsilva.music.jobs;

import dev.davidsilva.music.album.Album;
import dev.davidsilva.music.album.AlbumRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Collections;

@Configuration
public class ImportAlbumsJobConfiguration {
    private final PlatformTransactionManager transactionManager;
    private final AlbumRepository albumRepository;
    private final JobRepository jobRepository;
    private final JobLauncher jobLauncher;

    public ImportAlbumsJobConfiguration(
            AlbumRepository albumRepository,
            JobRepository jobRepository,
            @Qualifier("appDbTransactionManager") PlatformTransactionManager transactionManager,
            JobLauncher jobLauncher) {
        this.albumRepository = albumRepository;
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
        this.jobLauncher = jobLauncher;
    }

    public RepositoryItemReader<Album> printAlbumReader() {
        return new RepositoryItemReaderBuilder<Album>()
                .name("printAlbumReader")
                .repository(albumRepository)
                .methodName("findAll")
                .pageSize(100)
                .sorts(Collections.singletonMap("id", Sort.Direction.ASC))
                .build();
    }

    public ItemProcessor<Album, Album> printAlbumProcessor() {
        return album -> album; // Pass through
    }

    public ItemWriter<Album> printAlbumWriter() {
        return items -> {
            for (Album album : items) {
                System.out.println("Album ID: " + album.getId());
            }
        };
    }

    public Step printAlbumIdsStep() {
        return new StepBuilder("printAlbumIdsStep", jobRepository)
                .<Album, Album>chunk(10, transactionManager)
                .reader(printAlbumReader())
                .processor(printAlbumProcessor())
                .writer(printAlbumWriter())
                .build();
    }

    public Job printAlbumIdsJob() {
        return new JobBuilder("printAlbumIdsJob", jobRepository)
                .start(printAlbumIdsStep())
                .build();
    }

    @Bean
    public CommandLineRunner printAlbumJobRunner() {
        return args -> {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();
            jobLauncher.run(printAlbumIdsJob(), jobParameters);
        };
    }
}