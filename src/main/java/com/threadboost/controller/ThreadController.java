package com.threadboost.controller;

import com.threadboost.dto.request.ThreadPoolConfigRequest;
import com.threadboost.dto.response.ThreadMetricsResponse;
import com.threadboost.execution.ThreadPoolManager;
import com.threadboost.service.ExecutorStatsService;
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

    public ThreadController(ExecutorStatsService executorStatsService, ThreadPoolManager threadPoolManager) {
        this.executorStatsService = executorStatsService;
        this.threadPoolManager = threadPoolManager;
    }

    @GetMapping("/metrics")
    public ResponseEntity<ThreadMetricsResponse> getThreadMetrics() {
        return ResponseEntity.ok(executorStatsService.getThreadMetrics());
    }

    @PostMapping("/config")
    public ResponseEntity<ThreadMetricsResponse> updateThreadConfig(@Valid @RequestBody ThreadPoolConfigRequest request) {
        return ResponseEntity.ok(threadPoolManager.updateConfig(request));
    }
}
