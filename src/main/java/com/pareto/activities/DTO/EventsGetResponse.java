package com.pareto.activities.DTO;

import com.pareto.activities.enums.EParticipantCategory;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class EventsGetResponse {
    private String title;
    private String description;
    private String place;
    private List<EParticipantCategory> participantCategories;
    private String category;
    private String subCategory;
}
