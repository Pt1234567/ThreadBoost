package com.threadboost.distributed;

import com.threadboost.dto.response.RateLimitResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TokenBucketRateLimiterTest {

    @Test
    void allow_afterCapacityIsUsed_returnsFalse() {
        TokenBucketRateLimiter rateLimiter = new TokenBucketRateLimiter();
        RateLimitResponse response = null;

        for (int i = 0; i < 101; i++) {
            response = rateLimiter.allow("client-a");
        }

        assertThat(response).isNotNull();
        assertThat(response.allowed()).isFalse();
        assertThat(response.remainingTokens()).isZero();
    }
}
