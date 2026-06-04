package com.threadboost.dto.request;

import jakarta.validation.constraints.NotBlank;

public record WorkloadClassificationRequest(
        @NotBlank(message = "description is required")
        String description
) {
}
