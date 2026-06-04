package com.threadboost.execution;

import com.threadboost.domain.entity.Job;
import com.threadboost.dto.request.ThreadPoolConfigRequest;
import com.threadboost.dto.response.ThreadMetricsResponse;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class ThreadPoolManager {

    private static final Logger log = LoggerFactory.getLogger(ThreadPoolManager.class);

    private final AtomicLong sequence = new AtomicLong();
    private final AtomicLong rejectedTasks = new AtomicLong();
    private final ThreadPoolExecutor executor;

    public ThreadPoolManager() {
        executor = new ThreadPoolExecutor(
                2,
                8,
                30,
                TimeUnit.SECONDS,
                new PriorityBlockingQueue<>(),
                threadFactory(),
                rejectionHandler()
        );
    }

    public void execute(Job job, Runnable work) {
        CompletableFuture<Void> result = new CompletableFuture<>();
        ThreadBoostTask task = new ThreadBoostTask(job.getPriority(), sequence.incrementAndGet(), () -> {
            try {
                work.run();
                result.complete(null);
            } catch (RuntimeException ex) {
                result.completeExceptionally(ex);
            }
        });

        executor.execute(task);
        result.join();
    }

    public ThreadMetricsResponse metrics() {
        return new ThreadMetricsResponse(
                executor.getCorePoolSize(),
                executor.getMaximumPoolSize(),
                executor.getActiveCount(),
                executor.getPoolSize(),
                executor.getQueue().size(),
                executor.getCompletedTaskCount(),
                rejectedTasks.get()
        );
    }

    public ThreadMetricsResponse updateConfig(ThreadPoolConfigRequest request) {
        int currentCore = executor.getCorePoolSize();
        int currentMax = executor.getMaximumPoolSize();
        int nextCore = request.corePoolSize() == null ? currentCore : request.corePoolSize();
        int nextMax = request.maxPoolSize() == null ? currentMax : request.maxPoolSize();

        if (nextCore > nextMax) {
            throw new IllegalArgumentException("corePoolSize cannot be greater than maxPoolSize");
        }

        if (nextMax < currentCore) {
            executor.setCorePoolSize(nextCore);
            executor.setMaximumPoolSize(nextMax);
        } else {
            executor.setMaximumPoolSize(nextMax);
            executor.setCorePoolSize(nextCore);
        }

        log.info("Thread pool config updated corePoolSize={} maxPoolSize={}", nextCore, nextMax);
        return metrics();
    }

    @PreDestroy
    void shutdown() {
        executor.shutdown();
    }

    private ThreadFactory threadFactory() {
        AtomicLong counter = new AtomicLong();
        return runnable -> {
            Thread thread = new Thread(runnable);
            thread.setName("threadboost-worker-" + counter.incrementAndGet());
            thread.setUncaughtExceptionHandler((t, ex) ->
                    log.error("Uncaught worker error thread={}", t.getName(), ex));
            return thread;
        };
    }

    private RejectedExecutionHandler rejectionHandler() {
        return (runnable, currentExecutor) -> {
            rejectedTasks.incrementAndGet();
            throw new java.util.concurrent.RejectedExecutionException("ThreadBoost executor rejected task");
        };
    }
}
