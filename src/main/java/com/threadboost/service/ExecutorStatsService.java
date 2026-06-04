package com.threadboost.service;

import com.threadboost.dto.response.ExecutorStatsResponse;
import com.threadboost.execution.ExecutionTracker;
import org.springframework.stereotype.Service;

@Service
public class ExecutorStatsService {

    private static final String MODE = "SIMPLE_SYNC";

    private final ExecutionTracker executionTracker;

    public ExecutorStatsService(ExecutionTracker executionTracker) {
        this.executionTracker = executionTracker;
    }

    public ExecutorStatsResponse getStats() {
        return new ExecutorStatsResponse(
                MODE,
                executionTracker.getExecutedJobCount(),
                executionTracker.getActiveJobCount(),
                Runtime.getRuntime().availableProcessors()
        );
    }
}
