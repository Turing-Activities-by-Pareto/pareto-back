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

    @Mapping(target = "categoryId", source = "category")
    @Mapping(target = "subCategoryId", source = "subCategory")
    EventEntity toEventEntity(EventRequest eventRequest);

    @Mapping(target = "category", source = "categoryId")
    @Mapping(target = "subCategory", source = "subCategoryId")
    EventCreateResponse toEventCreateResponse(EventEntity eventEntity);


    EventGetResponse toEventGetResponse(
            EventEntity eventEntity
    );

    EventsGetResponse toEventsGetResponse(
            EventEntity eventEntity
    );
}
