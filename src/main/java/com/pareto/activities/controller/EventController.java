package com.pareto.activities.controller;

import com.pareto.activities.DTO.EventRequest;
import com.pareto.activities.DTO.EventResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {

    @PostMapping
    public EventResponse createEvent(
        @RequestBody EventRequest event
    ) {
        return new EventResponse();
    }
}
