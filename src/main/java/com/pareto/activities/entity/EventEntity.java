package com.pareto.activities.entity;

import com.pareto.activities.enums.EConfirmStatus;
import com.pareto.activities.enums.EParticipantCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Set;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventEntity {

    @Id
    private String id;
    private String userId;
    private String title;
    private String description;

    @Indexed
    private String category;
    @Indexed
    private String subCategory;

    private String place;
    private LocalDateTime deadline;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private EConfirmStatus confirmStatus;

    private String fileId;

    private Set<String> eventRequestIds;

    private Set<EParticipantCategory> participantCategories;
}
