package com.pareto.activities.DTO;

import com.pareto.activities.enums.EParticipantCategory;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class EventRequest {

    @NotEmpty
    private String userId;
    @NotEmpty
    private String title;
    @NotEmpty
    private String description;
    @NotEmpty
    private String category;
    @NotEmpty
    private String subCategory;
    @NotEmpty
    private String place;
    private Set<EParticipantCategory> participantCategories;
    @NotNull
    private LocalDateTime deadline;
    @NotNull
    private LocalDateTime startDate;
    @NotNull
    private LocalDateTime endDate;
    @NotEmpty
    private String fileExtension;
}
