package com.threadboost.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record VirtualThreadDemoRequest(
        @Min(value = 1, message = "taskCount must be at least 1")
        @Max(value = 10_000, message = "taskCount must be 10000 or less")
        Integer taskCount
) {
}
