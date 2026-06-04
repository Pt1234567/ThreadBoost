package com.threadboost.dto.response;

public record CompletableFuturePipelineResponse(
        String userData,
        String analytics,
        String report,
        String notification,
        long elapsedMillis
) {
}
