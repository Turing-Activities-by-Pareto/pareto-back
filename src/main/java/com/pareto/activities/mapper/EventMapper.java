package com.pareto.activities.mapper;

import com.pareto.activities.DTO.EventCreateResponse;
import com.pareto.activities.DTO.EventGetResponse;
import com.pareto.activities.DTO.EventRequest;
import com.pareto.activities.DTO.EventsGetResponse;
import com.pareto.activities.entity.EventEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;


@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface EventMapper {

    @Mapping(target = "category", ignore = true)
    @Mapping(target = "subCategory", ignore = true)
    EventEntity toEventEntity(EventRequest eventRequest);

    @Mapping(target = "category", ignore = true)
    @Mapping(target = "subCategory", ignore = true)
    EventCreateResponse toEventCreateResponse(EventEntity eventEntity);

    @Mapping(target = "category", ignore = true)
    @Mapping(target = "subCategory", ignore = true)
    EventGetResponse toEventGetResponse(EventEntity eventEntity);

    @Mapping(target = "category", ignore = true)
    @Mapping(target = "subCategory", ignore = true)
    EventsGetResponse toEventsGetResponse(EventEntity eventEntity);
}
