package com.threadboost.execution;

import com.threadboost.domain.entity.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SimpleJobExecutor implements JobExecutor {

    private static final Logger log = LoggerFactory.getLogger(SimpleJobExecutor.class);

    private final ExecutionTracker executionTracker;
    private final ThreadPoolManager threadPoolManager;

    public SimpleJobExecutor(ExecutionTracker executionTracker, ThreadPoolManager threadPoolManager) {
        this.executionTracker = executionTracker;
        this.threadPoolManager = threadPoolManager;
    }

    @Override
    public void execute(Job job) {
        executionTracker.recordStarted();
        try {
            threadPoolManager.execute(job, () ->
                    log.info("Executing job id={} name={} priority={}", job.getId(), job.getName(), job.getPriority()));
        } finally {
            executionTracker.recordFinished();
        }
    }
}
