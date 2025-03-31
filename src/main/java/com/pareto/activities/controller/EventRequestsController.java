package com.pareto.activities.controller;

import com.pareto.activities.DTO.EvReqResponse;
import com.pareto.activities.enums.ERequestStatus;
import com.pareto.activities.service.EventRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
public class EventRequestsController {

    private final EventRequestService eventRequestService;

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<EvReqResponse> getEventRequests(
            @PathVariable Long eventId
    ) {
        return eventRequestService.getEventRequests();
    }

    @GetMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EvReqResponse getEventRequestById(
            @PathVariable Long eventId
    ) {
        return eventRequestService.getEventRequestById(eventId);
    }

    @PatchMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EvReqResponse setStatus(
            @PathVariable Long eventId,
            @RequestBody ERequestStatus status
    ) {
        return eventRequestService.setStatus(eventId, status);
    }
}
