package com.threadboost.service;

import com.threadboost.domain.entity.Job;
import com.threadboost.domain.enums.JobStatus;
import com.threadboost.dto.request.CreateJobRequest;
import com.threadboost.dto.response.JobCreatedResponse;
import com.threadboost.exception.ResourceNotFoundException;
import com.threadboost.mapper.JobMapper;
import com.threadboost.repository.JobRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JobServiceTest {

    @Mock
    private JobRepository jobRepository;

    private final JobMapper jobMapper = new JobMapper();

    private JobService jobService;

    @BeforeEach
    void setUp() {
        jobService = new JobService(jobRepository, jobMapper);
    }

    @Test
    void createJob_savesAndReturnsPendingStatus() {
        CreateJobRequest request = new CreateJobRequest("Generate customer reports");

        when(jobRepository.save(any(Job.class))).thenAnswer(invocation -> {
            Job job = invocation.getArgument(0);
            job.setId(UUID.randomUUID());
            return job;
        });

        JobCreatedResponse result = jobService.createJob(request);

        assertThat(result.status()).isEqualTo(JobStatus.PENDING);
        assertThat(result.id()).isNotNull();
        verify(jobRepository).save(any(Job.class));
    }

    @Test
    void getJob_whenMissing_throwsNotFound() {
        UUID id = UUID.randomUUID();
        when(jobRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> jobService.getJob(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(id.toString());
    }
}
