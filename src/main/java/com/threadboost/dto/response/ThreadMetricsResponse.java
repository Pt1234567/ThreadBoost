package com.threadboost.dto.response;

public record ThreadMetricsResponse(
        int corePoolSize,
        int maximumPoolSize,
        int activeThreads,
        int poolSize,
        int queueSize,
        long completedTasks,
        long rejectedTasks
) {
}
