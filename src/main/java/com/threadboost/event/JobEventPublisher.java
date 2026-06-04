package com.threadboost.event;

import com.threadboost.domain.entity.Job;

public interface JobEventPublisher {

    void publishJobCreated(Job job);

    void publishJobCompleted(Job job);

    void publishJobFailed(Job job);
}
