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
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Collections;
import java.util.List;

@Configuration
public class AlbumBatchConfiguration {

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Bean
    public RepositoryItemReader<Album> albumReader() {
        return new RepositoryItemReaderBuilder<Album>()
                .name("albumReader")
                .repository(albumRepository)
                .methodName("findAll")
                .pageSize(100)
                .sorts(Collections.singletonMap("id", Sort.Direction.ASC))
                .build();
    }

    @Bean
    public ItemProcessor<Album, Album> albumProcessor() {
        return album -> {
            // Just pass through the album, no processing needed
            return album;
        };
    }

    @Bean
    public ItemWriter<Album> albumWriter() {
        return items -> {
            for (Album album : items) {
                System.out.println("Album ID: " + album.getId());
            }
        };
    }

    @Bean
    public Step printAlbumIdsStep() {
        return new StepBuilder("printAlbumIdsStep", jobRepository)
                .<Album, Album>chunk(10, transactionManager)
                .reader(albumReader())
                .processor(albumProcessor())
                .writer(albumWriter())
                .build();
    }

    @Bean
    public Job printAlbumIdsJob() {
        return new JobBuilder("printAlbumIdsJob", jobRepository)
                .start(printAlbumIdsStep())
                .build();
    }

    @Bean
    public CommandLineRunner jobRunner(JobLauncher jobLauncher, Job printAlbumIdsJob) {
        return args -> {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();
            jobLauncher.run(printAlbumIdsJob, jobParameters);
        };
    }
}
