package com.threadboost.dto.response;

public record ThreadHealthResponse(
        int totalThreads,
        int runnable,
        int blocked,
        int waiting,
        int timedWaiting,
        int terminated
) {
}
