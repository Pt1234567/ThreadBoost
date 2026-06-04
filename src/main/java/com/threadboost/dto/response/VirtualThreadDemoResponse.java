package com.threadboost.dto.response;

public record VirtualThreadDemoResponse(
        int taskCount,
        long elapsedMillis,
        String executor,
        String note
) {
}
