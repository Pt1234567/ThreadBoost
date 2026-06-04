package com.threadboost.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record ThreadPoolConfigRequest(
        @Min(value = 1, message = "corePoolSize must be at least 1")
        @Max(value = 100, message = "corePoolSize must be 100 or less")
        Integer corePoolSize,

        @Min(value = 1, message = "maxPoolSize must be at least 1")
        @Max(value = 200, message = "maxPoolSize must be 200 or less")
        Integer maxPoolSize
) {
}
