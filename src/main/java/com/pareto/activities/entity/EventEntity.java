package com.pareto.activities.entity;

import com.pareto.activities.enums.EConfirmStatus;
import com.pareto.activities.enums.EParticipantCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Document
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventEntity {

    @Id
    private String id;
    private UserEntity user;
    private String title;
    private String description;
    private String category;
    private String subCategory;
    private String place;
    private LocalDateTime deadline;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private EConfirmStatus confirmStatus;
    private FileEntity file;

    @DBRef
    private List<EventRequestEntity> requests;

    private Set<EParticipantCategory> participantCategories;
}
