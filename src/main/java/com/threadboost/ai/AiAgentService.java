package com.threadboost.ai;

import com.threadboost.domain.enums.ExecutionStrategy;
import com.threadboost.domain.enums.JobType;
import com.threadboost.domain.enums.Priority;
import com.threadboost.dto.request.FailureAnalysisRequest;
import com.threadboost.dto.request.OptimizerRequest;
import com.threadboost.dto.request.WorkloadClassificationRequest;
import com.threadboost.dto.response.FailureAnalysisResponse;
import com.threadboost.dto.response.OptimizerResponse;
import com.threadboost.dto.response.WorkloadClassificationResponse;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class AiAgentService {

    public WorkloadClassificationResponse classify(WorkloadClassificationRequest request) {
        String text = request.description().toLowerCase(Locale.ROOT);

        if (text.contains("email") || text.contains("notify")) {
            return new WorkloadClassificationResponse(
                    JobType.EMAIL,
                    Priority.HIGH,
                    ExecutionStrategy.VIRTUAL_THREAD,
                    "Email and notification jobs are usually IO-heavy."
            );
        }

        if (text.contains("report")) {
            return new WorkloadClassificationResponse(
                    JobType.REPORT,
                    Priority.HIGH,
                    ExecutionStrategy.THREAD_POOL,
                    "Report jobs usually combine CPU and IO work."
            );
        }

        if (text.contains("analytics") || text.contains("aggregate")) {
            return new WorkloadClassificationResponse(
                    JobType.ANALYTICS,
                    Priority.MEDIUM,
                    ExecutionStrategy.FORK_JOIN,
                    "Analytics work can often be split into parallel chunks."
            );
        }

        return new WorkloadClassificationResponse(
                JobType.GENERIC,
                Priority.MEDIUM,
                ExecutionStrategy.THREAD_POOL,
                "Default strategy for unknown workloads."
        );
    }

    public OptimizerResponse optimize(OptimizerRequest request) {
        if (request.cpuUsage() >= 80 || request.queueSize() >= 1_000) {
            return new OptimizerResponse(
                    ExecutionStrategy.KAFKA_WORKER,
                    "High pressure detected; buffer work through workers."
            );
        }

        if (request.activeThreads() >= 200) {
            return new OptimizerResponse(
                    ExecutionStrategy.VIRTUAL_THREAD,
                    "Many active threads suggests IO-heavy work may benefit from virtual threads."
            );
        }

        return new OptimizerResponse(
                ExecutionStrategy.THREAD_POOL,
                "System pressure is normal; keep using the tuned thread pool."
        );
    }

    public FailureAnalysisResponse analyzeFailure(FailureAnalysisRequest request) {
        String logs = request.logs().toLowerCase(Locale.ROOT);

        if (logs.contains("deadlock")) {
            return new FailureAnalysisResponse(
                    "Possible lock ordering issue",
                    "Inspect thread dump and enforce consistent lock acquisition order."
            );
        }

        if (logs.contains("rejectedexecutionexception")) {
            return new FailureAnalysisResponse(
                    "Executor rejected tasks",
                    "Increase queue capacity, tune pool size, or add backpressure."
            );
        }

        if (logs.contains("timeout")) {
            return new FailureAnalysisResponse(
                    "Downstream timeout",
                    "Add timeout metrics, retries with backoff, and circuit breaking."
            );
        }

        return new FailureAnalysisResponse(
                "Unknown failure pattern",
                "Capture structured logs, metrics, and thread dump around the failure."
        );
    }
}
