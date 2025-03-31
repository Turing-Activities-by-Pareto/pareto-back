package com.pareto.activities.mapper;

import com.pareto.activities.DTO.EvReqResponse;
import com.pareto.activities.entity.EventRequestEntity;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface EventRequestMapper {
    EvReqResponse toEvReqResponse(EventRequestEntity eventRequestEntity);
}
