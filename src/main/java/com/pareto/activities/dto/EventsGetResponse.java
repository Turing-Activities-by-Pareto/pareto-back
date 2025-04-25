package com.pareto.activities.dto;

import com.pareto.activities.entity.ParticipantCategory;
import com.pareto.activities.enums.EParticipantCategory;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Builder
@Data
public class EventsGetResponse {
    private Long id;
    private String title;
    private String description;
    private Boolean isLocal;
    private String location;
    private String category;
    private Boolean unlimitedSeats;
    private Integer remainingSeats;
    private String subCategory;
    private String imageGetUrl;
    private Set<ParticipantCategory> participantCategories;
}
