package com.pareto.activities.dto;

import com.pareto.activities.enums.EParticipantCategory;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class EventsGetResponse {
    private Long id;
    private String title;
    private String description;
    private String place;
    private List<EParticipantCategory> participantCategories;
    private String category;
    private String subCategory;
}
