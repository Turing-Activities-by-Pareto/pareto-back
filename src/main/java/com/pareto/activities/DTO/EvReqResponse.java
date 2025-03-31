package com.pareto.activities.DTO;

import com.pareto.activities.enums.ERequestStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EvReqResponse {
    private Long eventId;
    private ERequestStatus status;
}
