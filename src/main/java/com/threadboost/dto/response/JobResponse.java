package com.threadboost.dto.response;

import com.threadboost.domain.enums.ExecutionStrategy;
import com.threadboost.domain.enums.JobStatus;
import com.threadboost.domain.enums.JobType;
import com.threadboost.domain.enums.Priority;

import java.time.Instant;
import java.util.UUID;

public record JobResponse(
        UUID id,
        String name,
        String description,
        JobType type,
        Priority priority,
        ExecutionStrategy strategy,
        JobStatus status,
        Integer retryCount,
        Instant createdAt,
        Instant startedAt,
        Instant completedAt,
        String failureReason
) {
}
