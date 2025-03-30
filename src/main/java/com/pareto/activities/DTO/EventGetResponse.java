package com.pareto.activities.DTO;

import com.pareto.activities.enums.EParticipantCategory;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class EventGetResponse {
    public String title;
    public String description;
    public String place;
    private String imageGetUrl;
    private List<EParticipantCategory> participantCategories;
    public String category;
    public String subCategory;
}
