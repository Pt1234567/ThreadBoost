package com.threadboost.dto.response;

import com.threadboost.domain.enums.ExecutionStrategy;
import com.threadboost.domain.enums.JobType;
import com.threadboost.domain.enums.Priority;

public record WorkloadClassificationResponse(
        JobType type,
        Priority priority,
        ExecutionStrategy executor,
        String reason
) {
}
