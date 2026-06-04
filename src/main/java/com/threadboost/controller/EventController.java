package com.threadboost.controller;

import com.threadboost.event.JobEvent;
import com.threadboost.service.EventService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public ResponseEntity<List<JobEvent>> getRecentEvents() {
        return ResponseEntity.ok(eventService.getRecentEvents());
    }
}
