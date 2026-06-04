package com.threadboost.distributed;

import com.threadboost.dto.response.DistributedLockResponse;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Service
public class DistributedLockService {

    private static final Duration DEFAULT_TTL = Duration.ofSeconds(30);

    private final Map<String, LockRecord> locks = new HashMap<>();

    public synchronized DistributedLockResponse tryLock(String lockKey, String owner) {
        Instant now = Instant.now();
        LockRecord existing = locks.get(lockKey);
        if (existing != null && existing.expiresAt().isAfter(now)) {
            return new DistributedLockResponse(lockKey, false, existing.owner());
        }

        locks.put(lockKey, new LockRecord(owner, now.plus(DEFAULT_TTL)));
        return new DistributedLockResponse(lockKey, true, owner);
    }

    public synchronized DistributedLockResponse release(String lockKey, String owner) {
        LockRecord existing = locks.get(lockKey);
        if (existing == null || !existing.owner().equals(owner)) {
            return new DistributedLockResponse(lockKey, false, existing == null ? null : existing.owner());
        }

        locks.remove(lockKey);
        return new DistributedLockResponse(lockKey, true, owner);
    }

    private record LockRecord(String owner, Instant expiresAt) {
    }
}
