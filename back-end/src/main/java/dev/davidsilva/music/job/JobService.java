package dev.davidsilva.music.job;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JobService {
    private final JobLauncher jobLauncher;
    private final ImportAlbumsJob importAlbumsJob;

    public void launchGetImportAlbumsJob() throws Exception {
        launchJob(importAlbumsJob.getImportAlbumsJob());
    }

    private void launchJob(Job job) throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();
        jobLauncher.run(job, jobParameters);
    }
}
