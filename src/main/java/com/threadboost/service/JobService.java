package com.threadboost.service;

import com.threadboost.domain.entity.Job;
import com.threadboost.dto.request.CreateJobRequest;
import com.threadboost.dto.response.JobCreatedResponse;
import com.threadboost.dto.response.JobResponse;
import com.threadboost.exception.ResourceNotFoundException;
import com.threadboost.mapper.JobMapper;
import com.threadboost.repository.JobRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class JobService {

    private static final Logger log = LoggerFactory.getLogger(JobService.class);

    private final JobRepository jobRepository;
    private final JobMapper jobMapper;

    public JobService(JobRepository jobRepository, JobMapper jobMapper) {
        this.jobRepository = jobRepository;
        this.jobMapper = jobMapper;
    }

    @Transactional
    public JobCreatedResponse createJob(CreateJobRequest request) {
        Job job = jobMapper.toEntity(request);
        Job saved = jobRepository.save(job);
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

    private Job findJobOrThrow(UUID id) {
        return jobRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + id));
    }
}
