package com.threadboost.event;

import com.threadboost.domain.enums.JobStatus;

import java.time.Instant;
import java.util.UUID;

public record JobEvent(
        String topic,
        UUID jobId,
        JobStatus status,
        String message,
        Instant occurredAt
) {
}
