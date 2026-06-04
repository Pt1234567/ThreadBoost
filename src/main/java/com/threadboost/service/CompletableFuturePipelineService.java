package com.threadboost.service;

import com.threadboost.dto.response.CompletableFuturePipelineResponse;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class CompletableFuturePipelineService {

    public CompletableFuturePipelineResponse generateReport() {
        long startedAt = System.currentTimeMillis();

        CompletableFuture<String> userDataFuture = CompletableFuture.supplyAsync(this::fetchUserData);
        CompletableFuture<String> analyticsFuture = CompletableFuture.supplyAsync(this::fetchAnalytics);

        String report = userDataFuture
                .thenCombine(analyticsFuture, this::generateReport)
                .thenApply(this::formatReport)
                .exceptionally(ex -> "Report failed: " + ex.getMessage())
                .join();

        String notification = CompletableFuture.completedFuture(report)
                .thenApply(this::notifyUser)
                .join();

        return new CompletableFuturePipelineResponse(
                userDataFuture.join(),
                analyticsFuture.join(),
                report,
                notification,
                System.currentTimeMillis() - startedAt
        );
    }

    private String fetchUserData() {
        return "user-data-ready";
    }

    private String fetchAnalytics() {
        return "analytics-ready";
    }

    private String generateReport(String userData, String analytics) {
        return userData + " + " + analytics;
    }

    private String formatReport(String report) {
        return "report{" + report + "}";
    }

    private String notifyUser(String report) {
        return "notification-sent-for-" + report;
    }
}
