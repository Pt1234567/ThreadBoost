package com.threadboost.service;

import com.threadboost.dto.response.ExecutorStatsResponse;
import com.threadboost.dto.response.ThreadMetricsResponse;
import com.threadboost.execution.ExecutionTracker;
import com.threadboost.execution.ThreadPoolManager;
import org.springframework.stereotype.Service;

@Service
public class ExecutorStatsService {

    private static final String MODE = "CUSTOM_THREAD_POOL";

    private final ExecutionTracker executionTracker;
    private final ThreadPoolManager threadPoolManager;

    public ExecutorStatsService(ExecutionTracker executionTracker, ThreadPoolManager threadPoolManager) {
        this.executionTracker = executionTracker;
        this.threadPoolManager = threadPoolManager;
    }

    public ExecutorStatsResponse getStats() {
        return new ExecutorStatsResponse(
                MODE,
                executionTracker.getExecutedJobCount(),
                executionTracker.getActiveJobCount(),
                Runtime.getRuntime().availableProcessors()
        );
    }

    public ThreadMetricsResponse getThreadMetrics() {
        return threadPoolManager.metrics();
    }
}
