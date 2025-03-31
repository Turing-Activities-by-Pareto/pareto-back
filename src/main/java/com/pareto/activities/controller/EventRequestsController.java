package com.pareto.activities.controller;

import com.pareto.activities.DTO.EvReqResponse;
import com.pareto.activities.service.EventRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
public class EventRequestsController {

    private final EventRequestService eventRequestService;

    @GetMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EvReqResponse getEventRequestById(
            @PathVariable Long eventId
    ) {
        return eventRequestService.getEventRequestById(eventId);
    }
}
