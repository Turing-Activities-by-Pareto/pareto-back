package com.pareto.activities.DTO;

import com.pareto.activities.enums.ERequestStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EvReqResponse {
    private String eventId;
    private ERequestStatus status;
    private String userId;
}
