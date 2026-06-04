package com.threadboost.dto.response;

public record FailureAnalysisResponse(
        String rootCause,
        String solution
) {
}
