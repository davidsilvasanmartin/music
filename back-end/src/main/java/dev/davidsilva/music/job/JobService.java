package dev.davidsilva.music.job;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class JobService {
    private final JobLauncher jobLauncher;
    private final ImportAlbumsJob importAlbumsJob;

    private final JobExplorer jobExplorer;

    public void launchGetImportAlbumsJob() throws Exception {
        launchJob(importAlbumsJob.getImportAlbumsJob());
    }

    private void launchJob(Job job) throws Exception {
        // Check if a job execution is already running for the given job name.
        Set<JobExecution> runningJobExecutions = jobExplorer.findRunningJobExecutions(job.getName());
        if (!runningJobExecutions.isEmpty()) {
            throw new JobAlreadyRunningException(job.getName());
        }

        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();
        jobLauncher.run(job, jobParameters);
    }
}
