package com.threadboost.dto.response;

import java.util.List;

public record DeadlockResponse(
        boolean deadlockDetected,
        int deadlockedThreadCount,
        List<String> threadNames
) {
}
