package com.threadboost.execution;

import com.threadboost.domain.enums.Priority;

final class ThreadBoostTask implements Runnable, Comparable<ThreadBoostTask> {

    private final Priority priority;
    private final long sequence;
    private final Runnable delegate;

    ThreadBoostTask(Priority priority, long sequence, Runnable delegate) {
        this.priority = priority == null ? Priority.MEDIUM : priority;
        this.sequence = sequence;
        this.delegate = delegate;
    }

    @Override
    public void run() {
        delegate.run();
    }

    @Override
    public int compareTo(ThreadBoostTask other) {
        int priorityCompare = Integer.compare(weight(priority), weight(other.priority));
        if (priorityCompare != 0) {
            return priorityCompare;
        }
        return Long.compare(sequence, other.sequence);
    }

    private int weight(Priority priority) {
        return switch (priority) {
            case HIGH -> 0;
            case MEDIUM -> 1;
            case LOW -> 2;
        };
    }
}
