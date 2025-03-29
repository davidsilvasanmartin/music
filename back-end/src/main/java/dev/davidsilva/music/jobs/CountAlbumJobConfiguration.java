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
import java.util.concurrent.atomic.AtomicInteger;

@Configuration
public class CountAlbumJobConfiguration {
    private final PlatformTransactionManager transactionManager;
    private final AlbumRepository albumRepository;
    private final JobRepository jobRepository;
    private final JobLauncher jobLauncher;

    public CountAlbumJobConfiguration(
            AlbumRepository albumRepository,
            JobRepository jobRepository,
            @Qualifier("appDbTransactionManager") PlatformTransactionManager transactionManager,
            JobLauncher jobLauncher) {
        this.albumRepository = albumRepository;
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
        this.jobLauncher = jobLauncher;
    }

    @Bean
    public RepositoryItemReader<Album> countAlbumReader() {
        return new RepositoryItemReaderBuilder<Album>()
                .name("countAlbumReader")
                .repository(albumRepository)
                .methodName("findAll")
                .pageSize(100)
                .sorts(Collections.singletonMap("id", Sort.Direction.ASC))
                .build();
    }

    @Bean
    public ItemProcessor<Album, Album> countAlbumProcessor() {
        return album -> album; // Pass through
    }

    @Bean
    public ItemWriter<Album> countAlbumWriter() {
        AtomicInteger counter = new AtomicInteger(0);

        return items -> {
            int batchSize = items.size();
            int currentCount = counter.addAndGet(batchSize);
            System.out.println("Processed batch of " + batchSize + " albums, total count: " + currentCount);
        };
    }

    @Bean
    public Step countAlbumsStep() {
        return new StepBuilder("countAlbumsStep", jobRepository)
                .<Album, Album>chunk(10, transactionManager)
                .reader(countAlbumReader())
                .processor(countAlbumProcessor())
                .writer(countAlbumWriter())
                .build();
    }

    @Bean
    public Job countAlbumsJob() {
        return new JobBuilder("countAlbumsJob", jobRepository)
                .start(countAlbumsStep())
                .build();
    }

    @Bean
    public CommandLineRunner countAlbumJobRunner() {
        return args -> {
            // Only run this job if a specific argument is passed
            if (args.length > 0 && "count".equals(args[0])) {
                JobParameters jobParameters = new JobParametersBuilder()
                        .addLong("time", System.currentTimeMillis())
                        .toJobParameters();
                jobLauncher.run(countAlbumsJob(), jobParameters);
            }
        };
    }
}