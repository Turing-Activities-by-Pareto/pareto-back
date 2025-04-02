package com.pareto.activities.mapper.impl;

import com.pareto.activities.DTO.EvReqResponse;
import com.pareto.activities.entity.EventRequestEntity;
import com.pareto.activities.mapper.IEventRequestMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service("eventRequestMapper")
public class EventRequestMapper implements IEventRequestMapper {

    @Qualifier("IEventRequestMapperImpl")
    private final IEventRequestMapper mapper;

    @Override
    public EvReqResponse toEvReqResponse(EventRequestEntity eventRequestEntity) {

        EvReqResponse response = mapper.toEvReqResponse(eventRequestEntity);

        response.setUserId(eventRequestEntity
                                   .getUser()
                                   .getId());

        return response;
    }
}
