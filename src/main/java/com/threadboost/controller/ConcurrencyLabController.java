package com.threadboost.controller;

import com.threadboost.dto.request.VirtualThreadDemoRequest;
import com.threadboost.dto.response.CompletableFuturePipelineResponse;
import com.threadboost.dto.response.VirtualThreadDemoResponse;
import com.threadboost.service.CompletableFuturePipelineService;
import com.threadboost.service.VirtualThreadDemoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/lab")
public class ConcurrencyLabController {

    private final CompletableFuturePipelineService completableFuturePipelineService;
    private final VirtualThreadDemoService virtualThreadDemoService;

    public ConcurrencyLabController(
            CompletableFuturePipelineService completableFuturePipelineService,
            VirtualThreadDemoService virtualThreadDemoService
    ) {
        this.completableFuturePipelineService = completableFuturePipelineService;
        this.virtualThreadDemoService = virtualThreadDemoService;
    }

    @PostMapping("/completable-future/report")
    public ResponseEntity<CompletableFuturePipelineResponse> runCompletableFuturePipeline() {
        return ResponseEntity.ok(completableFuturePipelineService.generateReport());
    }

    @PostMapping("/virtual-threads/demo")
    public ResponseEntity<VirtualThreadDemoResponse> runVirtualThreadDemo(
            @Valid @RequestBody VirtualThreadDemoRequest request
    ) {
        return ResponseEntity.ok(virtualThreadDemoService.runDemo(request));
    }
}
