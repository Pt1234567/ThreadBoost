package com.threadboost.service;

import com.threadboost.dto.response.RaceSimulationResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class RaceSimulationService {

    private static final int THREADS = 8;
    private static final int INCREMENTS_PER_THREAD = 2_000;

    public RaceSimulationResponse runSimulation() {
        Counter counter = new Counter();
        runThreads(() -> {
            for (int i = 0; i < INCREMENTS_PER_THREAD; i++) {
                counter.incrementUnsafe();
                counter.incrementAtomic();
                counter.incrementWithLock();
            }
        });

        return new RaceSimulationResponse(
                THREADS,
                INCREMENTS_PER_THREAD,
                THREADS * INCREMENTS_PER_THREAD,
                counter.unsafeCount,
                counter.atomicCount.get(),
                counter.lockedCount
        );
    }

    private void runThreads(Runnable work) {
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < THREADS; i++) {
            Thread thread = new Thread(work, "race-simulation-" + i);
            threads.add(thread);
            thread.start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException("Race simulation interrupted", ex);
            }
        }
    }

    private static class Counter {
        private int unsafeCount;
        private final AtomicInteger atomicCount = new AtomicInteger();
        private int lockedCount;
        private final ReentrantLock lock = new ReentrantLock();

        void incrementUnsafe() {
            unsafeCount++;
        }

        void incrementAtomic() {
            atomicCount.incrementAndGet();
        }

        void incrementWithLock() {
            lock.lock();
            try {
                lockedCount++;
            } finally {
                lock.unlock();
            }
        }
    }
}
