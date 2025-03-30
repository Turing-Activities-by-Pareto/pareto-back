package com.pareto.activities.controller;

import com.pareto.activities.DTO.EventGetResponse;
import com.pareto.activities.DTO.EventRequest;
import com.pareto.activities.DTO.EventCreateResponse;
import com.pareto.activities.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventCreateResponse createEvent(
        @RequestBody EventRequest event
    ) {
        return eventService.createEvent(event);
    }


    @GetMapping(value = "/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventGetResponse getEvent(
            @PathVariable Long eventId
    ) {
        return eventService.getEventById(eventId);
    }
}
