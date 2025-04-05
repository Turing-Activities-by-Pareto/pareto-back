package com.pareto.activities.mapper;

import com.pareto.activities.DTO.EventCreateResponse;
import com.pareto.activities.DTO.EventGetResponse;
import com.pareto.activities.DTO.EventRequest;
import com.pareto.activities.DTO.EventsGetResponse;
import com.pareto.activities.entity.EventEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ON_IMPLICIT_CONVERSION)
public interface EventMapper {

    EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "confirmStatus", ignore = true)
    @Mapping(target = "fileId", ignore = true)
    @Mapping(target = "eventRequestIds", ignore = true)
    EventEntity toEventEntity(EventRequest eventRequest);


    @Mapping(target = "imageUploadUrl", ignore = true)
    EventCreateResponse toEventCreateResponse(EventEntity eventEntity);


    @Mapping(target = "imageGetUrl", ignore = true)
    EventGetResponse toEventGetResponse(
            EventEntity eventEntity
    );

    EventsGetResponse toEventsGetResponse(
            EventEntity eventEntity
    );
}
