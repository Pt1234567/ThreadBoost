package com.threadboost.controller;

import com.threadboost.distributed.DistributedLockService;
import com.threadboost.distributed.TokenBucketRateLimiter;
import com.threadboost.dto.request.RateLimitRequest;
import com.threadboost.dto.response.DistributedLockResponse;
import com.threadboost.dto.response.RateLimitResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/distributed")
public class DistributedController {

    private final DistributedLockService distributedLockService;
    private final TokenBucketRateLimiter rateLimiter;

    public DistributedController(DistributedLockService distributedLockService, TokenBucketRateLimiter rateLimiter) {
        this.distributedLockService = distributedLockService;
        this.rateLimiter = rateLimiter;
    }

    @PostMapping("/locks/{lockKey}/acquire")
    public ResponseEntity<DistributedLockResponse> acquireLock(
            @PathVariable String lockKey,
            @RequestHeader(defaultValue = "local-worker") String owner
    ) {
        return ResponseEntity.ok(distributedLockService.tryLock(lockKey, owner));
    }

    @PostMapping("/locks/{lockKey}/release")
    public ResponseEntity<DistributedLockResponse> releaseLock(
            @PathVariable String lockKey,
            @RequestHeader(defaultValue = "local-worker") String owner
    ) {
        return ResponseEntity.ok(distributedLockService.release(lockKey, owner));
    }

    @PostMapping("/rate-limit/check")
    public ResponseEntity<RateLimitResponse> checkRateLimit(@Valid @RequestBody RateLimitRequest request) {
        return ResponseEntity.ok(rateLimiter.allow(request.clientId()));
    }
}
