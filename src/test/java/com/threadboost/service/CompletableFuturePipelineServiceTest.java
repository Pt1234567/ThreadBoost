package com.threadboost.service;

import com.threadboost.dto.response.CompletableFuturePipelineResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CompletableFuturePipelineServiceTest {

    private final CompletableFuturePipelineService service = new CompletableFuturePipelineService();

    @Test
    void generateReport_combinesUserDataAndAnalytics() {
        CompletableFuturePipelineResponse response = service.generateReport();

        assertThat(response.userData()).isEqualTo("user-data-ready");
        assertThat(response.analytics()).isEqualTo("analytics-ready");
        assertThat(response.report()).contains("user-data-ready", "analytics-ready");
        assertThat(response.notification()).startsWith("notification-sent-for-");
    }
}
