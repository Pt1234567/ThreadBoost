package com.threadboost.mapper;

import com.threadboost.domain.entity.Job;
import com.threadboost.domain.enums.ExecutionStrategy;
import com.threadboost.domain.enums.JobStatus;
import com.threadboost.domain.enums.JobType;
import com.threadboost.domain.enums.Priority;
import com.threadboost.dto.request.CreateJobRequest;
import com.threadboost.dto.response.JobCreatedResponse;
import com.threadboost.dto.response.JobResponse;
import org.springframework.stereotype.Component;

@Component
public class JobMapper {

    public Job toEntity(CreateJobRequest request) {
        String description = request.description().trim();
        Job job = new Job();
        job.setName(deriveName(description));
        job.setDescription(description);
        job.setType(JobType.GENERIC);
        job.setPriority(Priority.MEDIUM);
        job.setStrategy(ExecutionStrategy.THREAD_POOL);
        job.setStatus(JobStatus.PENDING);
        job.setRetryCount(0);
        return job;
    }

    public JobCreatedResponse toCreatedResponse(Job job) {
        return new JobCreatedResponse(job.getId(), job.getStatus());
    }

    public JobResponse toResponse(Job job) {
        return new JobResponse(
                job.getId(),
                job.getName(),
                job.getDescription(),
                job.getType(),
                job.getPriority(),
                job.getStrategy(),
                job.getStatus(),
                job.getRetryCount(),
                job.getCreatedAt(),
                job.getStartedAt(),
                job.getCompletedAt(),
                job.getFailureReason()
        );
    }

    private String deriveName(String description) {
        if (description.length() <= 80) {
            return description;
        }
        return description.substring(0, 77) + "...";
    }
}
