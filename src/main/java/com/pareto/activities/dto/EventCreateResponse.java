package com.pareto.activities.dto;

import com.pareto.activities.entity.ParticipantCategory;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Builder
@Data
public class EventCreateResponse {

    private Long id;
    private String title;
    private String description;
    private Boolean isLocal;
    private String location;
    private Set<String> participantCategories;
    private String category;
    private String subCategory;
    private Boolean unlimitedSeats;
    private Integer remainingSeats;
    private String imageUploadUrl;

}
