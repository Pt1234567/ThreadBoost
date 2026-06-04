package com.threadboost.service;

import com.threadboost.domain.entity.Job;
import com.threadboost.domain.enums.JobStatus;
import com.threadboost.dto.request.CreateJobRequest;
import com.threadboost.dto.response.JobCreatedResponse;
import com.threadboost.dto.response.JobResponse;
import com.threadboost.exception.InvalidJobStateException;
import com.threadboost.exception.ResourceNotFoundException;
import com.threadboost.event.JobEventPublisher;
import com.threadboost.execution.JobExecutor;
import com.threadboost.mapper.JobMapper;
import com.threadboost.repository.JobRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class JobService {

    private static final Logger log = LoggerFactory.getLogger(JobService.class);

    private final JobRepository jobRepository;
    private final JobMapper jobMapper;
    private final JobExecutor jobExecutor;
    private final JobEventPublisher jobEventPublisher;

    public JobService(
            JobRepository jobRepository,
            JobMapper jobMapper,
            JobExecutor jobExecutor,
            JobEventPublisher jobEventPublisher
    ) {
        this.jobRepository = jobRepository;
        this.jobMapper = jobMapper;
        this.jobExecutor = jobExecutor;
        this.jobEventPublisher = jobEventPublisher;
    }

    @Transactional
    public JobCreatedResponse createJob(CreateJobRequest request) {
        Job job = jobMapper.toEntity(request);
        Job saved = jobRepository.save(job);
        jobEventPublisher.publishJobCreated(saved);
        log.info("Job created id={} status={}", saved.getId(), saved.getStatus());
        return jobMapper.toCreatedResponse(saved);
    }

    @Transactional(readOnly = true)
    public JobResponse getJob(UUID id) {
        Job job = findJobOrThrow(id);
        return jobMapper.toResponse(job);
    }

    @Transactional(readOnly = true)
    public List<JobResponse> getAllJobs() {
        return jobRepository.findAll().stream()
                .map(jobMapper::toResponse)
                .toList();
    }

    @Transactional
    public void deleteJob(UUID id) {
        Job job = findJobOrThrow(id);
        jobRepository.delete(job);
        log.info("Job deleted id={}", id);
    }

    @Transactional
    public JobResponse executeJob(UUID id) {
        Job job = findJobOrThrow(id);
        if (job.getStatus() != JobStatus.PENDING) {
            throw new InvalidJobStateException("Only PENDING jobs can be executed. Current status: " + job.getStatus());
        }

        try {
            job.setStatus(JobStatus.RUNNING);
            job.setStartedAt(Instant.now());
            jobExecutor.execute(job);
            job.setStatus(JobStatus.SUCCESS);
            job.setCompletedAt(Instant.now());
            job.setFailureReason(null);
            jobEventPublisher.publishJobCompleted(job);
            log.info("Job executed id={} status={}", job.getId(), job.getStatus());
        } catch (RuntimeException ex) {
            job.setStatus(JobStatus.FAILED);
            job.setCompletedAt(Instant.now());
            job.setFailureReason(ex.getMessage());
            jobEventPublisher.publishJobFailed(job);
            log.warn("Job failed id={} reason={}", job.getId(), ex.getMessage());
        }

        return jobMapper.toResponse(job);
    }

    private Job findJobOrThrow(UUID id) {
        return jobRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + id));
    }
}
