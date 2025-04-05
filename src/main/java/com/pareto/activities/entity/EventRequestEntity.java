package com.pareto.activities.entity;

import com.pareto.activities.enums.ERequestStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
public class EventRequestEntity {

    @Id
    private String id;

    private String userId;
    private String eventId;

    private ERequestStatus status;
    private LocalDateTime requestDate;
}
