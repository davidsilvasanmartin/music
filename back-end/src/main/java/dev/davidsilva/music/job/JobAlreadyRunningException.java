package dev.davidsilva.music.job;

/**
 * Use this exception for jobs that should not be started if they are currently running.
 * <p>
 * For example, this can be used for the job that imports albums. The job is initiated via a POST request.
 * This job can possibly take hours for large databases, so we don't want to start it if the previous
 * iteration has not finished.
 */
public class JobAlreadyRunningException extends IllegalStateException {
    public JobAlreadyRunningException(String jobName) {
        super("Job " + jobName + " is already running");
    }
}
