package com.pareto.activities.dto;

import com.pareto.activities.enums.EParticipantCategory;
import com.pareto.activities.validators.annotation.Category;
import com.pareto.activities.validators.annotation.CategoryValidator;
import com.pareto.activities.validators.annotation.SubCategory;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@CategoryValidator
@AllArgsConstructor
@NoArgsConstructor
public class EventRequest {

    @NotEmpty
    private String title;

    @NotEmpty
    private String description;

    @NotEmpty
    @Category
    private String category;

    @NotEmpty
    @SubCategory
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
