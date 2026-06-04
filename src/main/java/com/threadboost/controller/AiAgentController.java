package com.threadboost.controller;

import com.threadboost.ai.AiAgentService;
import com.threadboost.dto.request.FailureAnalysisRequest;
import com.threadboost.dto.request.OptimizerRequest;
import com.threadboost.dto.request.WorkloadClassificationRequest;
import com.threadboost.dto.response.FailureAnalysisResponse;
import com.threadboost.dto.response.OptimizerResponse;
import com.threadboost.dto.response.WorkloadClassificationResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
public class AiAgentController {

    private final AiAgentService aiAgentService;

    public AiAgentController(AiAgentService aiAgentService) {
        this.aiAgentService = aiAgentService;
    }

    @PostMapping("/classify")
    public ResponseEntity<WorkloadClassificationResponse> classify(
            @Valid @RequestBody WorkloadClassificationRequest request
    ) {
        return ResponseEntity.ok(aiAgentService.classify(request));
    }

    @PostMapping("/optimize")
    public ResponseEntity<OptimizerResponse> optimize(@Valid @RequestBody OptimizerRequest request) {
        return ResponseEntity.ok(aiAgentService.optimize(request));
    }

    @PostMapping("/failures/analyze")
    public ResponseEntity<FailureAnalysisResponse> analyzeFailure(
            @Valid @RequestBody FailureAnalysisRequest request
    ) {
        return ResponseEntity.ok(aiAgentService.analyzeFailure(request));
    }
}
