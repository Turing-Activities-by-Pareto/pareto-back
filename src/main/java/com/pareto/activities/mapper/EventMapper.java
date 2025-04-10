package com.pareto.activities.mapper;

import com.pareto.activities.dto.EventCreateResponse;
import com.pareto.activities.dto.EventGetResponse;
import com.pareto.activities.dto.EventRequest;
import com.pareto.activities.dto.EventsGetResponse;
import com.pareto.activities.entity.EventEntity;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ON_IMPLICIT_CONVERSION)
public interface EventMapper {

    EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);

    EventEntity toEventEntity(
            EventRequest eventRequest
    );

    EventCreateResponse toEventCreateResponse(
            EventEntity eventEntity
    );


    EventGetResponse toEventGetResponse(
            EventEntity eventEntity
    );

    EventsGetResponse toEventsGetResponse(
            EventEntity eventEntity
    );
}
