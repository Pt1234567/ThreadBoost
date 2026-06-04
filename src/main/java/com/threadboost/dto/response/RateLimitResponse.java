package com.threadboost.dto.response;

public record RateLimitResponse(
        String clientId,
        boolean allowed,
        int remainingTokens
) {
}
