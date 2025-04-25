package com.pareto.activities.dto;

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

    @NotNull
    private Boolean isLocal;

    @NotEmpty
    private String location;

    @NotEmpty
    private Set<String> participantCategories;

    @NotNull
    private LocalDateTime deadline;

    @NotNull
    private LocalDateTime startDate;

    @NotNull
    private LocalDateTime endDate;

    @NotNull
    private Boolean unlimitedSeats;

    @NotNull
    private Integer totalSeats;

    @NotEmpty
    private String fileExtension;
}
