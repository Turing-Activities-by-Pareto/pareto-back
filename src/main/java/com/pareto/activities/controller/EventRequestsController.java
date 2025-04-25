package com.pareto.activities.controller;

import com.pareto.activities.aspect.HandleDuplication;
import com.pareto.activities.dto.EvReqResponse;
import com.pareto.activities.service.EventRequestService;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping("/requests")
@RequiredArgsConstructor
public class EventRequestsController {

    private final EventRequestService eventRequestService;

    @GetMapping
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
            @NotNull @Min(1) @PathVariable
            Long requestId
    ) {
        return eventRequestService.getEventRequestById(requestId);
    }

    @PutMapping("/{requestId}/approve")
    @ResponseStatus(HttpStatus.OK)
    @HandleDuplication
    public EvReqResponse approve(
            @NotNull @Min(1) @PathVariable
            Long requestId
    ) {
        return eventRequestService.approve(requestId);
    }

    @PutMapping("/{requestId}/decline")
    @ResponseStatus(HttpStatus.OK)
    public EvReqResponse decline(
            @NotNull @Min(1) @PathVariable
            Long requestId
    ) {
        return eventRequestService.decline(requestId);
    }
}
