package com.threadboost.domain.enums;

public enum ExecutionStrategy {
    SINGLE_THREAD,
    THREAD_POOL,
    VIRTUAL_THREAD,
    FORK_JOIN,
    KAFKA_WORKER
}
