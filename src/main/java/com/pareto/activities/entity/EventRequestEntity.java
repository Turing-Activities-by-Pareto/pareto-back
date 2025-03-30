package com.pareto.activities.entity;

import com.pareto.activities.enums.ERequestStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "event_request")
public class EventRequestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "_user_id", nullable = false, updatable = false)
    @ToString.Exclude
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false, updatable = false)
    @ToString.Exclude
    private EventEntity event;

    @Enumerated(EnumType.STRING)
    private ERequestStatus status;

    private LocalDateTime requestDate;
}
