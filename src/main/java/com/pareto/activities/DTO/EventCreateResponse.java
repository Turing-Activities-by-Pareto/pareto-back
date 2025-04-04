package com.pareto.activities.DTO;

import com.pareto.activities.enums.EParticipantCategory;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class EventCreateResponse {

    private String id;
    private String title;
    private String description;
    private String place;
    private String imageUploadUrl;
    private List<EParticipantCategory> participantCategories;
    private String category;
    private String subCategory;
}
