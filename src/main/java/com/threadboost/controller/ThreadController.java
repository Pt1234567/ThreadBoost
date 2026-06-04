package com.threadboost.controller;

import com.threadboost.dto.response.DeadlockResponse;
import com.threadboost.dto.response.ThreadHealthResponse;
import com.threadboost.dto.request.ThreadPoolConfigRequest;
import com.threadboost.dto.response.ThreadMetricsResponse;
import com.threadboost.execution.ThreadPoolManager;
import com.threadboost.service.ExecutorStatsService;
import com.threadboost.service.ThreadHealthAnalyzer;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/thread")
public class ThreadController {

    private final ExecutorStatsService executorStatsService;
    private final ThreadPoolManager threadPoolManager;
    private final ThreadHealthAnalyzer threadHealthAnalyzer;

    public ThreadController(
            ExecutorStatsService executorStatsService,
            ThreadPoolManager threadPoolManager,
            ThreadHealthAnalyzer threadHealthAnalyzer
    ) {
        this.executorStatsService = executorStatsService;
        this.threadPoolManager = threadPoolManager;
        this.threadHealthAnalyzer = threadHealthAnalyzer;
    }

    @GetMapping("/metrics")
    public ResponseEntity<ThreadMetricsResponse> getThreadMetrics() {
        return ResponseEntity.ok(executorStatsService.getThreadMetrics());
    }

    @PostMapping("/config")
    public ResponseEntity<ThreadMetricsResponse> updateThreadConfig(@Valid @RequestBody ThreadPoolConfigRequest request) {
        return ResponseEntity.ok(threadPoolManager.updateConfig(request));
    }

    @GetMapping("/health")
    public ResponseEntity<ThreadHealthResponse> getThreadHealth() {
        return ResponseEntity.ok(threadHealthAnalyzer.analyze());
    }

    @GetMapping("/deadlocks")
    public ResponseEntity<DeadlockResponse> getDeadlocks() {
        return ResponseEntity.ok(threadHealthAnalyzer.detectDeadlocks());
    }
}
