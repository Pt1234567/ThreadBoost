package com.threadboost.dto.response;

public record RaceSimulationResponse(
        int threads,
        int incrementsPerThread,
        int expectedCount,
        int unsafeCount,
        int atomicCount,
        int lockedCount
) {
}
