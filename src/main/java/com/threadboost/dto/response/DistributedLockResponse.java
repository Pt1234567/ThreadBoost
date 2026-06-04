package com.threadboost.dto.response;

public record DistributedLockResponse(
        String lockKey,
        boolean acquired,
        String owner
) {
}
