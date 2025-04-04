package com.pareto.activities.DTO;

import com.pareto.activities.enums.EParticipantCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventCreateResponse {

    private String id;
    private String title;
    private String description;
    private String place;
    private String imageUploadUrl;
    private Set<EParticipantCategory> participantCategories;
    private String category;
    private String subCategory;
}
