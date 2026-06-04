package com.threadboost.domain.entity;

import com.threadboost.domain.enums.ExecutionStrategy;
import com.threadboost.domain.enums.JobStatus;
import com.threadboost.domain.enums.JobType;
import com.threadboost.domain.enums.Priority;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "jobs")
public class Job {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 2000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JobType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Priority priority;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExecutionStrategy strategy;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JobStatus status;

    @Column(nullable = false)
    private Integer retryCount;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    private Instant startedAt;

    private Instant completedAt;

    @Column(length = 2000)
    private String failureReason;

    public Job() {
    }

    @PrePersist
    void onCreate() {
        if (id == null) {
            id = UUID.randomUUID();
        }
        if (createdAt == null) {
            createdAt = Instant.now();
        }
        if (retryCount == null) {
            retryCount = 0;
        }
        if (status == null) {
            status = JobStatus.PENDING;
        }
        if (type == null) {
            type = JobType.GENERIC;
        }
        if (priority == null) {
            priority = Priority.MEDIUM;
        }
        if (strategy == null) {
            strategy = ExecutionStrategy.THREAD_POOL;
        }
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public JobType getType() { return type; }
    public void setType(JobType type) { this.type = type; }
    public Priority getPriority() { return priority; }
    public void setPriority(Priority priority) { this.priority = priority; }
    public ExecutionStrategy getStrategy() { return strategy; }
    public void setStrategy(ExecutionStrategy strategy) { this.strategy = strategy; }
    public JobStatus getStatus() { return status; }
    public void setStatus(JobStatus status) { this.status = status; }
    public Integer getRetryCount() { return retryCount; }
    public void setRetryCount(Integer retryCount) { this.retryCount = retryCount; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getStartedAt() { return startedAt; }
    public void setStartedAt(Instant startedAt) { this.startedAt = startedAt; }
    public Instant getCompletedAt() { return completedAt; }
    public void setCompletedAt(Instant completedAt) { this.completedAt = completedAt; }
    public String getFailureReason() { return failureReason; }
    public void setFailureReason(String failureReason) { this.failureReason = failureReason; }
}
