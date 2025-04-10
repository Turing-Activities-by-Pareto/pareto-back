package com.pareto.activities.dto;

import com.pareto.activities.enums.ERequestStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EvReqResponse {
    private Long eventId;
    private ERequestStatus status;
    private Long userId;
}
