package com.threadboost.execution;

import com.threadboost.domain.entity.Job;

public interface JobExecutor {

    void execute(Job job);
}
