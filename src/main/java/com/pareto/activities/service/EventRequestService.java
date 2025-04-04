package com.pareto.activities.service;

import com.pareto.activities.DTO.EvReqResponse;
import com.pareto.activities.entity.EventEntity;
import com.pareto.activities.entity.EventRequestEntity;
import com.pareto.activities.enums.BusinessStatus;
import com.pareto.activities.enums.ERequestStatus;
import com.pareto.activities.exception.BusinessException;
import com.pareto.activities.mapper.IEventRequestMapper;
import com.pareto.activities.repository.EventRepository;
import com.pareto.activities.repository.EventRequestRepository;
import com.pareto.activities.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EventRequestService {
    private final EventRequestRepository eventRequestRepository;
    private final IEventRequestMapper IEventRequestMapper;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public EvReqResponse getEventRequestById(String eventId) {

        return eventRequestRepository
                .findById(eventId)
                .map(IEventRequestMapper::toEvReqResponse)
                .orElseThrow(
                        () -> new BusinessException(
                                BusinessStatus.EVENTREQUEST_NOT_FOUND,
                                HttpStatus.NOT_FOUND
                        ));
    }

    public Page<EvReqResponse> getEventRequests(
            int page,
            int size
    ) {
        return eventRequestRepository
                .findAll(
                        PageRequest.of(
                                page,
                                size
                        )
                )
                .map(IEventRequestMapper::toEvReqResponse);
    }


    public EvReqResponse requestParticipation(
            String eventId
    ) {
        EventEntity event = eventRepository
                .findById(eventId)
                .orElseThrow(() -> new BusinessException(
                        BusinessStatus.EVENT_NOT_FOUND,
                        HttpStatus.NOT_FOUND
                ));

        String userId = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal()
                .toString()
                ;

        EventRequestEntity eventRequestEntity = EventRequestEntity
                .builder()
                .eventId(event.getId())
                .status(ERequestStatus.PENDING)
                .userId(userId)
                .build()
                ;

        eventRequestRepository.save(eventRequestEntity);

        return IEventRequestMapper.toEvReqResponse(
                eventRequestRepository.save(eventRequestEntity)
        );
    }

    @Transactional
    public EvReqResponse decline(
            String requestId
    ) {
        EventRequestEntity event = eventRequestRepository
                .findById(requestId)
                .orElseThrow(() -> new BusinessException(
                        BusinessStatus.EVENTREQUEST_NOT_FOUND,
                        HttpStatus.NOT_FOUND
                ));

        event.setStatus(ERequestStatus.DECLINED);

        return IEventRequestMapper.toEvReqResponse(event);
    }

    @Transactional
    public EvReqResponse approve(
            String requestId
    ) {
        EventRequestEntity event = eventRequestRepository
                .findById(requestId)
                .orElseThrow(() -> new BusinessException(
                        BusinessStatus.EVENTREQUEST_NOT_FOUND,
                        HttpStatus.NOT_FOUND
                ));

        event.setStatus(ERequestStatus.APPROVED);

        return IEventRequestMapper.toEvReqResponse(event);
    }
}
