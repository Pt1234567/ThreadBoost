package com.threadboost.controller;

import com.threadboost.dto.response.ExecutorStatsResponse;
import com.threadboost.service.ExecutorStatsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/executor")
public class ExecutorController {

    private final ExecutorStatsService executorStatsService;

    public ExecutorController(ExecutorStatsService executorStatsService) {
        this.executorStatsService = executorStatsService;
    }

    @GetMapping("/stats")
    public ResponseEntity<ExecutorStatsResponse> getStats() {
        return ResponseEntity.ok(executorStatsService.getStats());
    }
}
