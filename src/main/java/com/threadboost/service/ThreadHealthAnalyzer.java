package com.threadboost.service;

import com.threadboost.dto.response.DeadlockResponse;
import com.threadboost.dto.response.ThreadHealthResponse;
import org.springframework.stereotype.Service;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.Arrays;
import java.util.List;

@Service
public class ThreadHealthAnalyzer {

    private final ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();

    public ThreadHealthResponse analyze() {
        ThreadInfo[] threadInfos = threadMXBean.dumpAllThreads(false, false);
        int runnable = 0;
        int blocked = 0;
        int waiting = 0;
        int timedWaiting = 0;
        int terminated = 0;

        for (ThreadInfo threadInfo : threadInfos) {
            switch (threadInfo.getThreadState()) {
                case RUNNABLE -> runnable++;
                case BLOCKED -> blocked++;
                case WAITING -> waiting++;
                case TIMED_WAITING -> timedWaiting++;
                case TERMINATED -> terminated++;
                default -> {
                }
            }
        }

        return new ThreadHealthResponse(threadInfos.length, runnable, blocked, waiting, timedWaiting, terminated);
    }

    public DeadlockResponse detectDeadlocks() {
        long[] threadIds = threadMXBean.findDeadlockedThreads();
        if (threadIds == null || threadIds.length == 0) {
            return new DeadlockResponse(false, 0, List.of());
        }

        List<String> threadNames = Arrays.stream(threadMXBean.getThreadInfo(threadIds))
                .map(ThreadInfo::getThreadName)
                .toList();
        return new DeadlockResponse(true, threadIds.length, threadNames);
    }
}
