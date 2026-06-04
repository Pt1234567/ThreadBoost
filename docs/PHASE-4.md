# Phase 4 - Distributed Features

This phase adds Redis-style concepts without requiring Redis during development.

## Current implementation

- `DistributedLockService`
- `TokenBucketRateLimiter`

Both are in-memory implementations. They are useful for learning the behavior first. A real Redis implementation can replace them later.

## APIs

```http
POST /api/distributed/locks/{lockKey}/acquire
```

Optional header:

```text
owner: worker-a
```

Returns whether the lock was acquired.

```http
POST /api/distributed/locks/{lockKey}/release
```

Releases the lock when the owner matches.

```http
POST /api/distributed/rate-limit/check
Content-Type: application/json

{
  "clientId": "user-123"
}
```

Uses a token bucket with 100 tokens and a refill rate of 100 tokens per minute.

## Why in-memory first?

Distributed locks and rate limiters are easy to misuse. Building the behavior locally makes the rule clear before adding Redis commands like `SET NX PX` or Lua scripts.

## Production Redis version

Lock:

```text
SET lock:job:{id} owner NX PX 30000
```

Rate limiter:

```text
token_bucket:{clientId}
```

Use Redis TTLs and atomic operations so multiple application instances share the same decision.

## Interview questions

1. Why does a lock need TTL?

If a worker crashes, the lock should eventually expire so the job is not stuck forever.

2. Why check lock owner before release?

Without ownership, one worker could accidentally release another worker's lock.

3. What does token bucket solve?

It allows short bursts while still enforcing a long-term request rate.
