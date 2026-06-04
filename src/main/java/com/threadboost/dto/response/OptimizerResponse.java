package com.threadboost.dto.response;

import com.threadboost.domain.enums.ExecutionStrategy;

public record OptimizerResponse(
        ExecutionStrategy executor,
        String reason
) {
}
