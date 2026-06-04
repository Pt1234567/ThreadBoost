package com.threadboost.dto.request;

import jakarta.validation.constraints.Min;

public record OptimizerRequest(
        @Min(value = 0, message = "cpuUsage must be 0 or greater")
        int cpuUsage,

        @Min(value = 0, message = "queueSize must be 0 or greater")
        int queueSize,

        @Min(value = 0, message = "activeThreads must be 0 or greater")
        int activeThreads
) {
}
