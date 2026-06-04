package com.threadboost.dto.request;

import jakarta.validation.constraints.NotBlank;

public record FailureAnalysisRequest(
        @NotBlank(message = "logs are required")
        String logs
) {
}
