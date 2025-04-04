package com.pareto.activities.DTO;

import com.pareto.activities.enums.EParticipantCategory;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class EventRequest {
    @NotEmpty
    private String title;
    private String description;
    private String category;
    private String subCategory;
    private String place;

    @NotNull
    private List<EParticipantCategory> participantCategories;

    @NotEmpty
    @FutureOrPresent
    private LocalDateTime deadline;

    @NotNull
    @FutureOrPresent
    private LocalDateTime startDate;

    @NotNull
    @PastOrPresent
    private LocalDateTime endDate;

    @NotEmpty
    private String fileExtension;
}
