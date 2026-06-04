package com.threadboost.dto.response;

import com.threadboost.domain.enums.JobStatus;

import java.util.UUID;

public record JobCreatedResponse(UUID id, JobStatus status) {
}
