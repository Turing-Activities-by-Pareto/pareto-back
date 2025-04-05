package com.pareto.activities.mapper;

import com.pareto.activities.DTO.EvReqResponse;
import com.pareto.activities.entity.EventRequestEntity;
import com.pareto.activities.repository.EventRequestRepository;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface IEventRequestMapper {

    EventRequestRepository eventRequestRepository = null;

    EvReqResponse toEvReqResponse(EventRequestEntity eventRequestEntity);

}
