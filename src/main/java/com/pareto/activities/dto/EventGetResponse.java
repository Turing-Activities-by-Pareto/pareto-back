package com.pareto.activities.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Builder
@Data
public class EventGetResponse {

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
    private String imageGetUrl;

}
