package com.threadboost.distributed;

import com.threadboost.dto.response.DistributedLockResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DistributedLockServiceTest {

    private final DistributedLockService distributedLockService = new DistributedLockService();

    @Test
    void tryLock_whenAlreadyOwned_returnsFalse() {
        DistributedLockResponse first = distributedLockService.tryLock("job-1", "worker-a");
        DistributedLockResponse second = distributedLockService.tryLock("job-1", "worker-b");

        assertThat(first.acquired()).isTrue();
        assertThat(second.acquired()).isFalse();
        assertThat(second.owner()).isEqualTo("worker-a");
    }

    @Test
    void release_whenOwnerMatches_removesLock() {
        distributedLockService.tryLock("job-1", "worker-a");

        DistributedLockResponse released = distributedLockService.release("job-1", "worker-a");
        DistributedLockResponse reacquired = distributedLockService.tryLock("job-1", "worker-b");

        assertThat(released.acquired()).isTrue();
        assertThat(reacquired.acquired()).isTrue();
        assertThat(reacquired.owner()).isEqualTo("worker-b");
    }
}
