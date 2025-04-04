package com.pareto.activities.DTO;

import com.pareto.activities.enums.EParticipantCategory;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class EventRequest {
    private String title;
    private String description;
    private String category;
    private String subCategory;
    private String place;
    private Set<EParticipantCategory> participantCategories;
    private LocalDateTime deadline;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String fileExtension;
}
