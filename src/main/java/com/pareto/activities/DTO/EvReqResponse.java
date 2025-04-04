package com.pareto.activities.DTO;

import com.pareto.activities.enums.ERequestStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EvReqResponse {
    private String eventId;
    private ERequestStatus status;
    private String userId;
}
