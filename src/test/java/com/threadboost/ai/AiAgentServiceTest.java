package com.threadboost.ai;

import com.threadboost.domain.enums.ExecutionStrategy;
import com.threadboost.domain.enums.JobType;
import com.threadboost.dto.request.FailureAnalysisRequest;
import com.threadboost.dto.request.OptimizerRequest;
import com.threadboost.dto.request.WorkloadClassificationRequest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AiAgentServiceTest {

    private final AiAgentService aiAgentService = new AiAgentService();

    @Test
    void classify_whenReportText_returnsReportClassification() {
        var response = aiAgentService.classify(new WorkloadClassificationRequest("Generate customer report"));

        assertThat(response.type()).isEqualTo(JobType.REPORT);
        assertThat(response.executor()).isEqualTo(ExecutionStrategy.THREAD_POOL);
    }

    @Test
    void optimize_whenQueueIsHigh_recommendsKafkaWorker() {
        var response = aiAgentService.optimize(new OptimizerRequest(70, 1_500, 100));

        assertThat(response.executor()).isEqualTo(ExecutionStrategy.KAFKA_WORKER);
    }

    @Test
    void analyzeFailure_whenRejectedExecution_returnsExecutorAdvice() {
        var response = aiAgentService.analyzeFailure(new FailureAnalysisRequest("RejectedExecutionException"));

        assertThat(response.rootCause()).contains("Executor");
    }
}
