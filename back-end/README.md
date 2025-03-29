# Back-end

## Decisions and notes about this project

### Spring Batch

#### Is manually creating Spring Batch tables a good practice?

Manually creating Spring Batch tables is actually a common and valid approach in several scenarios.

##### Advantages:

1. **Database-specific optimizations**: Your SQL script adapts Spring Batch tables for SQLite, which has different data
   types and constraints than the standard schemas.
2. **Control over schema evolution**: You can manage schema versions explicitly through migration scripts.
3. **Integration with existing database migration frameworks**: Works well with tools like Flyway or Liquibase.
4. **Custom naming conventions or storage requirements**: Allows for organization-specific table designs.

##### Potential Concerns:

1. **Maintenance overhead**: You'll need to update your tables if a future Spring Batch version changes the schema.
2. **Version compatibility**: Ensuring your custom tables remain compatible with Spring Batch's expectations.

##### SQLite-specific Considerations

Your SQL script appears to use SQLite-specific syntax (e.g., `AUTOINCREMENT`). If you're using SQLite in production with
Spring Batch, be aware of some limitations:

- SQLite has limited concurrency support which might affect batch job execution in multi-instance environments.
- SQLite's transaction isolation may differ from other databases Spring Batch typically runs on.

##### About reviewing SQL Schema Completeness

Your schema looks comprehensive, but verify it includes all required columns from the latest Spring Batch version you're
using. The standard schemas are available in the Spring Batch repository and might be worth comparing against.

#### Example of full-blown Job configuration

Consider the following configuration, and see the explanations below:

```java

@Configuration
public class AlbumBatchConfiguration {
    private final TransactionManager transactionManager;
    private final AlbumRepository albumRepository;
    private final JobRepository jobRepository;

    public AlbumBatchConfiguration(AlbumRepository albumRepository, JobRepository jobRepository, @Qualifier("appDbTransactionManager") PlatformTransactionManager transactionManager) {
        this.albumRepository = albumRepository;
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
    }

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
                .<Album, Album>chunk(10, (PlatformTransactionManager) transactionManager)
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
```

Spring Batch is a framework designed for high-volume, batch processing operations. Let me explain the key components and
how they interact in this configuration:

##### 1. Item Reader

**What it is**: A component that reads data from a source (database, file, etc.) one item at a time.
In this configuration:

- `albumReader()` creates a `RepositoryItemReader` that reads `Album` entities from the database
- It uses pagination (100 items per page) to handle large datasets efficiently
- Data is sorted by ID to ensure consistent ordering across job runs

##### 2. Item Processor

**What it is**: A component that applies business logic to transform or filter each item.
In this configuration:

- `albumProcessor()` defines a simple pass-through processor
- While this implementation doesn't modify the albums, it could be enhanced to:
    - Filter out certain albums
    - Transform album data
    - Enrich albums with additional information

##### 3. Item Writer

**What it is**: A component that writes processed items to a destination (database, file, etc.).
In this configuration:

- `albumWriter()` writes album information to the console
- It receives items in "chunks" rather than one at a time
- It could be modified to persist data, generate reports, or send notifications

##### 4. Step

**What it is**: A sequential phase in a batch job that combines a reader, processor, and writer.
In this configuration:

- `printAlbumIdsStep()` creates a step that:
    - Processes data in chunks of 10 items
    - Each chunk is processed within a transaction managed by the transaction manager
    - The step orchestrates the flow: reader → processor → writer

##### 5. Job

**What it is**: A complete batch process, composed of one or more steps executed in sequence.
In this configuration:

- `printAlbumIdsJob()` defines a job with a single step
- More complex jobs could have multiple steps with conditional flows

##### 6. JobLauncher, JobParameters, and CommandLineRunner

These components initiate and control job execution:

- `JobLauncher`: Executes a job with specific parameters
- `JobParameters`: Unique identifiers and parameters for a job execution
- `CommandLineRunner`: Spring Boot component that runs when the application starts

##### How It All Works Together

Here's the flow of data and control in the configured batch job:

1. **Job Initiation**:

- When the application starts, the `CommandLineRunner` bean is executed
- It creates job parameters with a timestamp to make each run unique
- It uses the `JobLauncher` to start the `printAlbumIdsJob`

2. **Job Execution**:

- The job repository tracks metadata (start time, status, etc.)
- The job executes its steps in sequence (just one in this case)

3. **Step Execution**:

- The step begins with transaction management
- It processes data in chunks of 10 items

4. **Data Processing Flow**:

- **Reading**: The reader fetches albums from the database in pages of 100
- **Processing**: Each album passes through the processor (unchanged)
- **Writing**: Every 10 processed albums are sent to the writer as a chunk
- The writer prints each album's ID to the console

5. **Completion**:

- After all albums are processed, the step completes
- The job completes and its status is recorded in the job repository

##### Key Relationships

- **Reader, Processor, Writer**: Form a pipeline for data processing within a step
- **Step**: Contains the reader, processor, writer, and manages chunk transactions
- **Job**: Contains and orchestrates steps
- **JobRepository**: Stores execution metadata for jobs and steps
- **JobLauncher**: Initiates job execution with specific parameters

##### Advantages of This Architecture

1. **Scalability**: Can handle large datasets through pagination and chunking
2. **Resilience**: Transactions provide rollback capabilities if errors occur
3. **Modularity**: Each component (reader, processor, writer) can be developed and tested independently
4. **Reusability**: Components can be reused across different batch jobs
5. **Monitoring**: Job execution metadata enables monitoring and troubleshooting

In this specific implementation, the job simply reads albums from a database and prints their IDs to the console, but
the same architecture could be extended to handle complex data transformations, multi-step workflows, and error handling
scenarios.

## TODO

### OAuth2

- Resources to check eventually
    - https://www.youtube.com/watch?v=7zm3mxaAFWk
- Look at how Immich has done Oauth2 (they're a client only I believe):
    - https://github.com/immich-app/immich/issues/33
    - https://github.com/immich-app/immich/pull/884
- On OAuth2 done wrong:
    - http://blog.intothesymmetry.com/2015/06/on-oauth-token-hijacks-for-fun-and.html

### General

- Any videos from these people are good
    - [Dan Vega](https://www.youtube.com/@DanVega/videos)
    - Gabriel Moiroux
    - Laurentiu Splica
    - Vlad Mihalcea
    - Thorbenn Jansen
    - [Coffee + Software](https://www.youtube.com/@coffeesoftware/videos)

## Info

### Spring Security

- Tip: the order of Spring Security filters can be found in the class
  ` org.springframework.security.config.annotation.web.builders.FilterOrderRegistration`.
- Trying to follow this guide at the moment https://spring.io/guides/tutorials/spring-security-and-angular-js

### Sessions

- For Spring Session with Redis, see: https://docs.spring.io/spring-session/reference/getting-started/using-redis.html
- Official docs for session management
    - https://docs.spring.io/spring-security/reference/servlet/authentication/persistence.html
    - https://docs.spring.io/spring-security/reference/servlet/authentication/session-management.html

### JPA/Hibernate

- Read "Java Persistence with Hibernate", published by Manning. Maybe there is a new edition, "Java Persistence with
  Spring Data".
