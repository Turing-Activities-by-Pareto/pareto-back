package com.pareto.activities.mapper;

import com.pareto.activities.DTO.EvReqResponse;
import com.pareto.activities.entity.EventRequestEntity;
import com.pareto.activities.enums.BusinessStatus;
import com.pareto.activities.exception.BusinessException;
import com.pareto.activities.repository.EventRequestRepository;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.springframework.http.HttpStatus;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface IEventRequestMapper {

    EventRequestRepository eventRequestRepository = null;

    @Mapping(target = "userId", ignore = true)
    EvReqResponse toEvReqResponse(EventRequestEntity eventRequestEntity);

}
