package com.pareto.activities.mapper;

import com.pareto.activities.dto.EvReqResponse;
import com.pareto.activities.entity.EventRequestEntity;
import com.pareto.activities.repository.EventRequestRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface IEventRequestMapper {

    EventRequestRepository eventRequestRepository = null;

    @Mapping(target = "userId", ignore = true)
    EvReqResponse toEvReqResponse(EventRequestEntity eventRequestEntity);

}
