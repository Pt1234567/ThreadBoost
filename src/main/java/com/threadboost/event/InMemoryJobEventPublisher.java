package com.threadboost.event;

import com.threadboost.domain.entity.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Component
public class InMemoryJobEventPublisher implements JobEventPublisher {

    public static final String JOB_CREATED = "job-created";
    public static final String JOB_COMPLETED = "job-completed";
    public static final String JOB_FAILED = "job-failed";

    private static final Logger log = LoggerFactory.getLogger(InMemoryJobEventPublisher.class);

    private final List<JobEvent> events = new ArrayList<>();

    @Override
    public void publishJobCreated(Job job) {
        publish(new JobEvent(JOB_CREATED, job.getId(), job.getStatus(), "Job accepted", Instant.now()));
    }

    @Override
    public void publishJobCompleted(Job job) {
        publish(new JobEvent(JOB_COMPLETED, job.getId(), job.getStatus(), "Job completed", Instant.now()));
    }

    @Override
    public void publishJobFailed(Job job) {
        publish(new JobEvent(JOB_FAILED, job.getId(), job.getStatus(), job.getFailureReason(), Instant.now()));
    }

    public synchronized List<JobEvent> getEvents() {
        return List.copyOf(events);
    }

    private synchronized void publish(JobEvent event) {
        events.add(event);
        log.info("Published local event topic={} jobId={} status={}", event.topic(), event.jobId(), event.status());
    }
}
