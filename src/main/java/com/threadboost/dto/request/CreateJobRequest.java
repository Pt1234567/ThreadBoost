package com.threadboost.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateJobRequest(
        @NotBlank(message = "description is required")
        @Size(min = 3, max = 2000, message = "description must be between 3 and 2000 characters")
        String description
) {
}
