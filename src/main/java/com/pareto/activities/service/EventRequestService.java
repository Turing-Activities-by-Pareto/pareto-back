package com.pareto.activities.service;

import com.pareto.activities.dto.EvReqResponse;
import com.pareto.activities.entity.EventEntity;
import com.pareto.activities.entity.EventRequestEntity;
import com.pareto.activities.entity.UserEntity;
import com.pareto.activities.enums.BusinessStatus;
import com.pareto.activities.enums.ERequestStatus;
import com.pareto.activities.exception.BusinessException;
import com.pareto.activities.mapper.manual.EventRequestMapper;
import com.pareto.activities.repository.EventRepository;
import com.pareto.activities.repository.EventRequestRepository;
import com.pareto.activities.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class EventRequestService {
    private final EventRequestRepository eventRequestRepository;
    private final EventRequestMapper eventRequestMapper;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public EvReqResponse getEventRequestById(Long eventId) {

        return eventRequestRepository
                .findById(eventId)
                .map(eventRequestMapper::toEvReqResponse)
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
                        PageRequest.of(page, size)
                )
                .map(eventRequestMapper::toEvReqResponse);
    }

    //this is named like this for future work (when event gets updated).
    @Transactional
    public void createOrUpdatePendingRequest(EventEntity event, UserEntity user, LocalDateTime createdAt) {
        EventRequestEntity request = new EventRequestEntity();
        request.addEvent(event);
        request.addUser(user);
        request.setStatus(ERequestStatus.PENDING);
        request.setRequestDate(createdAt);

        eventRequestRepository.save(request);
    }


    public EvReqResponse requestParticipation(
            Long eventId,
            Long userId) {

        EventEntity event = eventRepository
                .findById(eventId)
                .orElseThrow(() -> new BusinessException(
                        BusinessStatus.EVENT_NOT_FOUND,
                        HttpStatus.NOT_FOUND
                ));

        EventRequestEntity eventRequestEntity = EventRequestEntity
                .builder()
                .event(event)
                .status(ERequestStatus.PENDING)
                .user(userRepository
                              .findById(userId)
                              .orElseThrow(() -> new BusinessException(
                                      BusinessStatus.USER_NOT_FOUND,
                                      HttpStatus.NOT_FOUND
                              )))
                .build()
                ;

        eventRequestRepository.save(eventRequestEntity);

        return eventRequestMapper.toEvReqResponse(
                eventRequestRepository.save(eventRequestEntity)
        );
    }

    @Transactional
    public EvReqResponse decline(
            Long requestId
    ) {
        EventRequestEntity event = eventRequestRepository
                .findById(requestId)
                .orElseThrow(() -> new BusinessException(
                        BusinessStatus.EVENTREQUEST_NOT_FOUND,
                        HttpStatus.NOT_FOUND
                ));

        event.setStatus(ERequestStatus.DECLINED);

        return eventRequestMapper.toEvReqResponse(event);
    }

    @Transactional
    public EvReqResponse approve(
            Long requestId
    ) {
        EventRequestEntity event = eventRequestRepository
                .findById(requestId)
                .orElseThrow(() -> new BusinessException(
                        BusinessStatus.EVENTREQUEST_NOT_FOUND,
                        HttpStatus.NOT_FOUND
                ));

        event.setStatus(ERequestStatus.APPROVED);

        return eventRequestMapper.toEvReqResponse(event);
    }
}
