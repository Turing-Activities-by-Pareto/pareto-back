package com.pareto.activities.service;

import com.pareto.activities.DTO.EvReqResponse;
import com.pareto.activities.enums.BusinessStatus;
import com.pareto.activities.exception.BusinessException;
import com.pareto.activities.mapper.EventRequestMapper;
import com.pareto.activities.repository.EventRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EventRequestService {
    private final EventRequestRepository eventRequestRepository;
    private final EventRequestMapper eventRequestMapper;

    public EvReqResponse getEventRequestById(Long eventId) {

        return eventRequestRepository.findById(eventId)
                .map(eventRequestMapper::toEvReqResponse)
                .orElseThrow(() -> new BusinessException(BusinessStatus.EVENT_NOT_FOUND, HttpStatus.NOT_FOUND));

    }
}
