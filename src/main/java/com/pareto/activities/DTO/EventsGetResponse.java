package com.pareto.activities.DTO;

import com.pareto.activities.enums.EConfirmStatus;
import com.pareto.activities.enums.EParticipantCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventsGetResponse {
    private String id;
    private String title;
    private String description;
    private String place;
    private List<EParticipantCategory> participantCategories;
    private String category;
    private String subCategory;

    private LocalDateTime deadline;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private EConfirmStatus confirmStatus;
}
