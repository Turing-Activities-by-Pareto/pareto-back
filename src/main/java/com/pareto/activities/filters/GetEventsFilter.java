package com.pareto.activities.filters;

import com.pareto.activities.entity.EventRequestEntity;
import com.pareto.activities.enums.EConfirmStatus;
import com.pareto.activities.enums.EParticipantCategory;
import lombok.Builder;
import lombok.Data;
import org.springdoc.core.annotations.ParameterObject;

import java.time.LocalDateTime;
import java.util.Set;

@ParameterObject
@Data
@Builder
public class GetEventsFilter {

    private EConfirmStatus confirmStatus;

    private Set<EParticipantCategory> participantCategories;
    private Set<EventRequestEntity> requests;

	private LocalDateTime fromDeadline;
	private LocalDateTime toDeadline;
	private LocalDateTime fromEndDate;
	private LocalDateTime toEndDate;
	private LocalDateTime fromStartDate;
	private LocalDateTime toStartDate;

    private Long id;
    private Long userId;

    private String category;
    private String description;
    private String place;
    private String subCategory;
    private String title;
}
