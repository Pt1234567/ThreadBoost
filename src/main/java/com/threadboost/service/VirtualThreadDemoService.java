package com.threadboost.service;

import com.threadboost.dto.request.VirtualThreadDemoRequest;
import com.threadboost.dto.response.VirtualThreadDemoResponse;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

@Service
public class VirtualThreadDemoService {

    private static final int DEFAULT_TASK_COUNT = 100;

    public VirtualThreadDemoResponse runDemo(VirtualThreadDemoRequest request) {
        int taskCount = request.taskCount() == null ? DEFAULT_TASK_COUNT : request.taskCount();
        Instant startedAt = Instant.now();

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            List<Callable<Integer>> tasks = new ArrayList<>();
            for (int i = 0; i < taskCount; i++) {
                int taskNumber = i;
                tasks.add(() -> simulateIoWork(taskNumber));
            }

            for (var future : executor.invokeAll(tasks)) {
                future.get();
            }
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Virtual thread demo interrupted", ex);
        } catch (ExecutionException ex) {
            throw new IllegalStateException("Virtual thread demo failed", ex);
        }

        return new VirtualThreadDemoResponse(
                taskCount,
                Duration.between(startedAt, Instant.now()).toMillis(),
                "newVirtualThreadPerTaskExecutor",
                "Useful for many blocking IO-style tasks; not a replacement for CPU-bound parallelism."
        );
    }

    private Integer simulateIoWork(int taskNumber) throws InterruptedException {
        Thread.sleep(5);
        return taskNumber;
    }
}
