package com.pareto.activities.controller;

import com.pareto.activities.config.Constants;
import com.pareto.activities.dto.EvReqResponse;
import com.pareto.activities.dto.EventCreateResponse;
import com.pareto.activities.dto.EventFilter;
import com.pareto.activities.dto.EventGetResponse;
import com.pareto.activities.dto.EventRequest;
import com.pareto.activities.dto.EventsGetResponse;
import com.pareto.activities.aspect.HandleDuplication;
import com.pareto.activities.service.EventRequestService;
import com.pareto.activities.service.EventService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;
    private final EventRequestService eventRequestService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @HandleDuplication
    public EventCreateResponse createEvent(
            @NotNull
            @Min(1)
            Long userId,
            @RequestBody @Valid EventRequest event
    ) {
        return eventService.createEvent(event, userId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<EventGetResponse> getEvents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @ParameterObject EventFilter eventFilter
            ) {
        return eventService.getEventsPage(
                page,
                size,
                eventFilter
        );
    }

    @GetMapping(value = "/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventGetResponse getEvent(
            @NotNull @Min(1) @PathVariable
            Long eventId
    ) {
        EventGetResponse eventGetResponse = eventService.getEventById(eventId);
        eventGetResponse.setImageGetUrl(getEventImageGetUrl(eventId));
        return eventGetResponse;
    }

    @GetMapping(value = "/{eventId}/image-get-url")
    @ResponseStatus(HttpStatus.OK)
    public String getEventImageGetUrl(
            @NotNull @Min(1) @PathVariable
            Long eventId
    ) {
        return eventService.getObjectGetUrl(eventId);
    }

    @GetMapping(value = "/{eventId}/image-put-url")
    @ResponseStatus(HttpStatus.OK)
    public String getEventImagePutUrl(
            @NotNull @Min(1) @PathVariable
            Long eventId
    ) {
        return eventService.getObjectPutUrl(eventId);
    }

    @PostMapping("/{eventId}/request-participation")
    @ResponseStatus(HttpStatus.OK)
    @HandleDuplication
    public EvReqResponse setStatus(
            @NotNull @Min(1) @PathVariable
            Long eventId,
            @RequestHeader(Constants.USER_HEADER) Long userId
    ) {
        return eventRequestService.requestParticipation(
                eventId,
                userId
        );
    }
}
