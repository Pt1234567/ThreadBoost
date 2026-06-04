package com.threadboost.dto.response;

public record ExecutorStatsResponse(
        String mode,
        long executedJobCount,
        int activeJobCount,
        int availableProcessors
) {
}
