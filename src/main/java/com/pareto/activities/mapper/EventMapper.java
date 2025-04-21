package com.pareto.activities.mapper;

import com.pareto.activities.dto.EventCreateResponse;
import com.pareto.activities.dto.EventGetResponse;
import com.pareto.activities.dto.EventRequest;
import com.pareto.activities.dto.EventsGetResponse;
import com.pareto.activities.entity.EventEntity;
import com.pareto.activities.entity.ParticipantCategory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.factory.Mappers;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ON_IMPLICIT_CONVERSION)
public interface EventMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "confirmStatus", constant = "PENDING")
    @Mapping(target = "file", ignore = true)
    @Mapping(target = "requests", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "subCategory", ignore = true)
    @Mapping(target = "participantCategories", expression = "java(new java.util.HashSet<>())")
    @Mapping(target = "location", ignore = true)
    @Mapping(target = "remainingSeats", source = "totalSeats")
    EventEntity toEventEntity(EventRequest eventRequest);


    @Mapping(target = "imageUploadUrl", ignore = true)
    @Mapping(target = "category", source = "category.name")
    @Mapping(target = "subCategory", source = "subCategory.name")
    @Mapping(target = "location", source = "location.name")
    @Mapping(target = "participantCategories", ignore = true)
    EventCreateResponse toEventCreateResponse(EventEntity eventEntity);

    @Mapping(target = "imageGetUrl", ignore = true)
    @Mapping(target = "category", source = "category.name")
    @Mapping(target = "subCategory", source = "subCategory.name")
    @Mapping(target = "location", source = "location.name")
    @Mapping(target = "participantCategories", ignore = true)
    EventGetResponse toEventGetResponse(
            EventEntity eventEntity
    );

    @Mapping(target = "category", source = "category.name")
    @Mapping(target = "subCategory", source = "subCategory.name")
    @Mapping(target = "location", source = "location.name")
    EventsGetResponse toEventsGetResponse(
            EventEntity eventEntity
    );
}
