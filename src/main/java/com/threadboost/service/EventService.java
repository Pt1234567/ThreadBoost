package com.threadboost.service;

import com.threadboost.event.InMemoryJobEventPublisher;
import com.threadboost.event.JobEvent;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    private final InMemoryJobEventPublisher eventPublisher;

    public EventService(InMemoryJobEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public List<JobEvent> getRecentEvents() {
        return eventPublisher.getEvents();
    }
}
