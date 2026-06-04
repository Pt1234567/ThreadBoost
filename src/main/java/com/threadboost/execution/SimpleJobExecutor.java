package com.threadboost.execution;

import com.threadboost.domain.entity.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SimpleJobExecutor implements JobExecutor {

    private static final Logger log = LoggerFactory.getLogger(SimpleJobExecutor.class);

    @Override
    public void execute(Job job) {
        log.info("Executing job id={} name={}", job.getId(), job.getName());
    }
}
