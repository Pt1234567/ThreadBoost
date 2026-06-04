package com.threadboost.distributed;

import com.threadboost.dto.response.RateLimitResponse;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Service
public class TokenBucketRateLimiter {

    private static final int CAPACITY = 100;
    private static final int REFILL_PER_MINUTE = 100;

    private final Map<String, Bucket> buckets = new HashMap<>();

    public synchronized RateLimitResponse allow(String clientId) {
        Bucket bucket = buckets.computeIfAbsent(clientId, ignored -> new Bucket(CAPACITY, Instant.now()));
        refill(bucket);

        if (bucket.tokens() <= 0) {
            return new RateLimitResponse(clientId, false, 0);
        }

        bucket.consume();
        return new RateLimitResponse(clientId, true, bucket.tokens());
    }

    private void refill(Bucket bucket) {
        Instant now = Instant.now();
        long elapsedSeconds = now.getEpochSecond() - bucket.lastRefill().getEpochSecond();
        if (elapsedSeconds <= 0) {
            return;
        }

        int tokensToAdd = (int) ((elapsedSeconds * REFILL_PER_MINUTE) / 60);
        if (tokensToAdd > 0) {
            bucket.refill(tokensToAdd, now);
        }
    }

    private static class Bucket {
        private int tokens;
        private Instant lastRefill;

        Bucket(int tokens, Instant lastRefill) {
            this.tokens = tokens;
            this.lastRefill = lastRefill;
        }

        int tokens() {
            return tokens;
        }

        Instant lastRefill() {
            return lastRefill;
        }

        void consume() {
            tokens--;
        }

        void refill(int tokensToAdd, Instant refilledAt) {
            tokens = Math.min(CAPACITY, tokens + tokensToAdd);
            lastRefill = refilledAt;
        }
    }
}
