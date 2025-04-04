package com.pareto.activities.DTO;

import com.pareto.activities.enums.EConfirmStatus;
import com.pareto.activities.enums.EParticipantCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventCreateResponse {

    private String id;
    private String userId;
    private String title;
    private String description;
    private String category;
    private String subCategory;
    private String place;
    private LocalDateTime deadline;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private EConfirmStatus confirmStatus;
    private Set<EParticipantCategory> participantCategories;
    private String imageUploadUrl;
}
