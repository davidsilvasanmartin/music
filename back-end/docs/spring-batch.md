# Spring Batch

## Is manually creating Spring Batch tables a good practice?

Manually creating Spring Batch tables is actually a common and valid approach in several scenarios.

### Advantages:

1. **Database-specific optimizations**: Your SQL script adapts Spring Batch tables for SQLite, which has different data
   types and constraints than the standard schemas.
2. **Control over schema evolution**: You can manage schema versions explicitly through migration scripts.
3. **Integration with existing database migration frameworks**: Works well with tools like Flyway or Liquibase.
4. **Custom naming conventions or storage requirements**: Allows for organization-specific table designs.

### Potential Concerns:

1. **Maintenance overhead**: You'll need to update your tables if a future Spring Batch version changes the schema.
2. **Version compatibility**: Ensuring your custom tables remain compatible with Spring Batch's expectations.

### SQLite-specific Considerations

Your SQL script appears to use SQLite-specific syntax (e.g., `AUTOINCREMENT`). If you're using SQLite in production with
Spring Batch, be aware of some limitations:

- SQLite has limited concurrency support which might affect batch job execution in multi-instance environments.
- SQLite's transaction isolation may differ from other databases Spring Batch typically runs on.

### About reviewing SQL Schema Completeness

Your schema looks comprehensive, but verify it includes all required columns from the latest Spring Batch version you're
using. The standard schemas are available in the Spring Batch repository and might be worth comparing against.

## Example of full-blown Job configuration

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

### 1. Item Reader

**What it is**: A component that reads data from a source (database, file, etc.) one item at a time.
In this configuration:

- `albumReader()` creates a `RepositoryItemReader` that reads `Album` entities from the database
- It uses pagination (100 items per page) to handle large datasets efficiently
- Data is sorted by ID to ensure consistent ordering across job runs

### 2. Item Processor

**What it is**: A component that applies business logic to transform or filter each item.
In this configuration:

- `albumProcessor()` defines a simple pass-through processor
- While this implementation doesn't modify the albums, it could be enhanced to:
    - Filter out certain albums
    - Transform album data
    - Enrich albums with additional information

### 3. Item Writer

**What it is**: A component that writes processed items to a destination (database, file, etc.).
In this configuration:

- `albumWriter()` writes album information to the console
- It receives items in "chunks" rather than one at a time
- It could be modified to persist data, generate reports, or send notifications

### 4. Step

**What it is**: A sequential phase in a batch job that combines a reader, processor, and writer.
In this configuration:

- `printAlbumIdsStep()` creates a step that:
    - Processes data in chunks of 10 items
    - Each chunk is processed within a transaction managed by the transaction manager
    - The step orchestrates the flow: reader → processor → writer

### 5. Job

**What it is**: A complete batch process, composed of one or more steps executed in sequence.
In this configuration:

- `printAlbumIdsJob()` defines a job with a single step
- More complex jobs could have multiple steps with conditional flows

### 6. JobLauncher, JobParameters, and CommandLineRunner

These components initiate and control job execution:

- `JobLauncher`: Executes a job with specific parameters
- `JobParameters`: Unique identifiers and parameters for a job execution
- `CommandLineRunner`: Spring Boot component that runs when the application starts

### How It All Works Together

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

### Key Relationships

- **Reader, Processor, Writer**: Form a pipeline for data processing within a step
- **Step**: Contains the reader, processor, writer, and manages chunk transactions
- **Job**: Contains and orchestrates steps
- **JobRepository**: Stores execution metadata for jobs and steps
- **JobLauncher**: Initiates job execution with specific parameters

### Advantages of This Architecture

1. **Scalability**: Can handle large datasets through pagination and chunking
2. **Resilience**: Transactions provide rollback capabilities if errors occur
3. **Modularity**: Each component (reader, processor, writer) can be developed and tested independently
4. **Reusability**: Components can be reused across different batch jobs
5. **Monitoring**: Job execution metadata enables monitoring and troubleshooting

In this specific implementation, the job simply reads albums from a database and prints their IDs to the console, but
the same architecture could be extended to handle complex data transformations, multi-step workflows, and error handling
scenarios.

## Relationship between Page Size and Chunk Size in the previous example

There are two different sizes:

1. **Page Size (100)**: Set in the `albumReader()` method
2. **Chunk Size (10)**: Set in the `printAlbumIdsStep()` method

These two settings serve different purposes and operate at different layers of the batch processing.

### Page Size (Database Interaction)

The `pageSize(100)` in the `albumReader()` method:

- Controls how many records are fetched from the database in a single query
- Is an optimization for database interaction
- Reduces the number of database queries needed to fetch all data
- Spring Data's pagination is used behind the scenes

### Chunk Size (Transaction Boundary)

The `.chunk(10)` in the `printAlbumIdsStep()` method:

- Defines how many items are processed before a transaction is committed
- Controls the granularity of transaction boundaries
- Determines how many items are collected before being sent to the writer

### The Process Flow

Here's what happens during execution:

1. **Database Retrieval**:

- The reader fetches 100 albums at once from the database
- These albums are stored in the reader's internal buffer

2. **Item Processing**:

- The reader delivers items one at a time when requested by the step
- Each item goes through the processor individually

3. **Chunk Collection**:

- The step collects the processed items
- Once 10 items are collected (the chunk size), they are:
    - Sent as a batch to the writer
    - The transaction is committed (after items have been written)
    - Processing moves to the next chunk

4. **Buffer Replenishment**:

- When the internal buffer is depleted, the reader fetches the next 100 albums
- This continues until all albums are processed

### Why Have Different Sizes?

This two-level approach balances different optimization concerns:

- **Large Page Size (100)**:
    - Reduces database round trips
    - Optimizes database I/O
    - Reduces overall query overhead

- **Smaller Chunk Size (10)**:
    - Creates more frequent transaction commits
    - Reduces the amount of work lost if a transaction fails
    - Keeps memory usage lower
    - Provides more granular progress tracking

### Real Impact on Processing

In this specific configuration:

1. Albums are loaded from the database in batches of 100
2. The step processes them in groups of 10 for transaction management
3. Every 10 albums, the IDs are printed and the transaction commits
4. After 10 such chunks, a new database query would fetch the next 100 albums

This design pattern allows Spring Batch to handle very large datasets efficiently by:

- Minimizing database load with fewer queries
- Maintaining reasonable memory usage by processing in chunks
- Ensuring resilience by committing transactions frequently

It's a common and effective practice to have a larger page size than chunk size when designing batch jobs that need to
balance database efficiency with processing safety.

## Understanding Transaction Boundaries in Spring Batch Chunk Processing

In the previous section, we have said that "the transaction is committed after ItemsWriter has written the items in a
chunk". In this section, we explain this concept with an example.

In Spring Batch, for a chunk-based step:

1. A transaction is **started** at the beginning of processing a chunk
2. Items are read one by one
3. Each item is processed individually
4. All processed items are collected until the chunk size is reached
5. The entire chunk is sent to the writer
6. The writer processes all items in the chunk
7. The transaction is **committed** after the writer completes
8. Then the next chunk begins with a new transaction

Let me illustrate with a more concrete example - an album price update job:

``` java
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
public ItemProcessor<Album, Album> albumPriceUpdater() {
    return album -> {
        // Apply 10% discount to all albums
        double newPrice = album.getPrice() * 0.9;
        album.setPrice(newPrice);
        return album;
    };
}

@Bean
public ItemWriter<Album> albumWriter() {
    return items -> {
        for (Album album : items) {
            albumRepository.save(album); // Save the updated albums
            System.out.println("Updated album " + album.getId() + " price to: " + album.getPrice());
        }
    };
}

@Bean
public Step updateAlbumPricesStep() {
    return new StepBuilder("updateAlbumPricesStep", jobRepository)
            .<Album, Album>chunk(10, (PlatformTransactionManager) transactionManager)
            .reader(albumReader())
            .processor(albumPriceUpdater())
            .writer(albumWriter())
            .build();
}
```

### Transaction Flow in this Example

With a chunk size of 10, here's what happens:

1. A transaction begins
2. The reader fetches albums (possibly from its buffer of 100)
3. For each album (items 1-10):
    - It's processed by `albumPriceUpdater()` (price reduced by 10%)
    - The processed album is collected in memory
4. Once 10 albums are processed, the entire chunk is passed to `albumWriter()`
5. The writer saves all 10 updated albums to the database
6. The transaction is committed
7. A new transaction begins for the next chunk

### Why This Matters: Transaction Benefits

This transaction boundary provides several important benefits:

1. **Atomicity**: If anything fails during processing or writing of a chunk (e.g., album #8 causes an error), the entire
   chunk can be rolled back, leaving the database in a consistent state.
2. **Efficiency**: Committing transactions is relatively expensive in terms of performance. Doing it once per chunk (10
   items) rather than per item improves throughput.
3. **Restart Capability**: If the job fails, Spring Batch can track which chunks were successfully committed and restart
   from the failed chunk.

### Real-World Scenario

Imagine processing 10,000 albums with our price update job:

- Albums are read in pages of 100 from the database
- Processing occurs in chunks of 10 albums
- If an error occurs while processing album #8765:
    - The current chunk (albums #8761-8770) is rolled back
    - Albums #1-8760 remain updated (already committed)
    - The job can be restarted from album #8761

This provides both efficiency (bulk database operations) and safety (the ability to restart from failure points) without
having to reprocess the entire dataset.
Understanding these transaction boundaries is crucial when designing batch jobs, especially when dealing with data that
must maintain consistency and integrity across processing failures.

