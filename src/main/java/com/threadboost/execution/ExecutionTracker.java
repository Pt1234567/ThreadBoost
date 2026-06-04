package com.threadboost.execution;

import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class ExecutionTracker {

    private final AtomicLong executedJobCount = new AtomicLong();
    private final AtomicInteger activeJobCount = new AtomicInteger();

    public void recordStarted() {
        activeJobCount.incrementAndGet();
    }

    public void recordFinished() {
        activeJobCount.decrementAndGet();
        executedJobCount.incrementAndGet();
    }

    public long getExecutedJobCount() {
        return executedJobCount.get();
    }

    public int getActiveJobCount() {
        return activeJobCount.get();
    }
}
