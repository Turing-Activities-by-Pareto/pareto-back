package com.pareto.activities.mapper.manual;

import com.pareto.activities.dto.EvReqResponse;
import com.pareto.activities.entity.EventRequestEntity;
import org.springframework.stereotype.Component;

@Component
public class EventRequestMapper {

    public EvReqResponse toEvReqResponse(EventRequestEntity eventRequestEntity) {
        return EvReqResponse.builder()
                .eventId(eventRequestEntity.getId())
                .status(eventRequestEntity.getStatus())
                .userId(eventRequestEntity.getUser().getId())
                .build();
    }
}
