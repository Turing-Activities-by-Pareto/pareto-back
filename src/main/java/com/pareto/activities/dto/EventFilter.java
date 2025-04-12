package com.pareto.activities.dto;

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
public class EventFilter {
    private String username;
    private String title;
    //make categories enum
    private String category;
    private String subCategory;
    private String place;
    private List<EParticipantCategory> participantCategories;
    private LocalDateTime deadline;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
