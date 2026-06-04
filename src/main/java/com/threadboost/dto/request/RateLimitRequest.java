package com.threadboost.dto.request;

import jakarta.validation.constraints.NotBlank;

public record RateLimitRequest(
        @NotBlank(message = "clientId is required")
        String clientId
) {
}
