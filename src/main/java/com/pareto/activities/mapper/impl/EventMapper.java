package com.pareto.activities.mapper.impl;

import com.pareto.activities.DTO.EventCreateResponse;
import com.pareto.activities.DTO.EventGetResponse;
import com.pareto.activities.DTO.EventRequest;
import com.pareto.activities.DTO.EventsGetResponse;
import com.pareto.activities.entity.EventEntity;
import com.pareto.activities.enums.BusinessStatus;
import com.pareto.activities.exception.BusinessException;
import com.pareto.activities.mapper.IEventMapper;
import com.pareto.activities.repository.EventCategoryRepository;
import com.pareto.activities.repository.SubEventCategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Slf4j
@Service("eventMapper")
public class EventMapper implements IEventMapper {

    @Qualifier("IEventMapperImpl")
    private final IEventMapper mapper;
    private final SubEventCategoryRepository subEventCategoryRepository;
    private final EventCategoryRepository eventCategoryRepository;

    @Override
    public EventEntity toEventEntity(
            EventRequest request
    ) {
        EventEntity entity = mapper.toEventEntity(request);

        entity.setCategory(eventCategoryRepository
                                   .findByName(request.getCategory())
                                   .orElseThrow(() -> new BusinessException(
                                                        "Event category not found : " + request.getCategory(),
                                                        BusinessStatus.EVENT_CATEGORY_NOT_FOUND,
                                                        HttpStatus.NOT_FOUND
                                                )
                                   )
        );

        if (subEventCategoryRepository
                .findByName(request.getSubCategory())
                .isEmpty()) {
            throw new BusinessException(
                    "Event sub category not found : " + request.getSubCategory(),
                    BusinessStatus.DATA_NOT_FOUND,
                    HttpStatus.NOT_FOUND
            );
        }

        if (
                !StringUtils.equals(
                        subEventCategoryRepository
                                .findByName(request.getSubCategory())
                                .get()
                                .getCategory()
                                .getName(),
                        request.getCategory()
                )
        ) {
            throw new BusinessException(
                    "sub category:%s and category:%s are not matched".formatted(request.getSubCategory(), request.getCategory()),
                    BusinessStatus.DATA_NOT_FOUND,
                    HttpStatus.NOT_FOUND
            );
        }

        entity.setSubCategory(subEventCategoryRepository
                                      .findByName(request.getSubCategory())
                                      .orElseThrow(() -> new BusinessException(
                                              "Event sub category not found : " + request.getSubCategory(),
                                              BusinessStatus.DATA_NOT_FOUND,
                                              HttpStatus.NOT_FOUND
                                      )));

        return entity;
    }

    @Override
    public EventCreateResponse toEventCreateResponse(EventEntity eventEntity) {
        EventCreateResponse eventCreateResponse = mapper.toEventCreateResponse(eventEntity);

        eventCreateResponse.setCategory(eventEntity
                                                .getCategory()
                                                .getName());

        return eventCreateResponse;
    }

    @Override
    public EventGetResponse toEventGetResponse(EventEntity eventEntity) {

        EventGetResponse response = mapper.toEventGetResponse(eventEntity);

        response.setCategory(eventEntity
                                     .getCategory()
                                     .getName());

        return response;
    }

    @Override
    public EventsGetResponse toEventsGetResponse(EventEntity eventEntity) {
        EventsGetResponse response = mapper.toEventsGetResponse(eventEntity);

        response.setCategory(eventEntity
                                     .getCategory()
                                     .getName());

        return response;
    }
}
