package com.pareto.activities.controller;

import com.pareto.activities.dto.EvReqResponse;
import com.pareto.activities.aspect.HandleDuplication;
import com.pareto.activities.enums.ERequestStatus;
import com.pareto.activities.service.EventRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
public class EventRequestsController {

    private final EventRequestService eventRequestService;

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public Page<EvReqResponse> getEventRequests(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return eventRequestService.getEventRequests(
                page,
                size
        );
    }

    @GetMapping("/{requestId}")
    @ResponseStatus(HttpStatus.OK)
    public EvReqResponse getEventRequestById(
            @PathVariable Long requestId
    ) {
        return eventRequestService.getEventRequestById(requestId);
    }

    @PostMapping("/approve/{requestId}")
    @ResponseStatus(HttpStatus.OK)
    @HandleDuplication
    public EvReqResponse approve(
            @PathVariable Long requestId,
            @RequestBody ERequestStatus status
    ) {
        return eventRequestService.approve(requestId);
    }

    @PostMapping("/decline/{requestId}")
    @ResponseStatus(HttpStatus.OK)
    public EvReqResponse decline(
            @PathVariable Long requestId,
            @RequestBody ERequestStatus status
    ) {
        return eventRequestService.decline(requestId);
    }
}
