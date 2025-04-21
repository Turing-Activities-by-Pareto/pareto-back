package com.pareto.activities.entity;

import com.pareto.activities.enums.ERequestStatus;
import jakarta.persistence.Column;
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
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "event_request")
@Builder
public class EventRequestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private long id;

    @ManyToOne
    @JoinColumn(name = "_user_id", nullable = false, updatable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false, updatable = false)
    private EventEntity event;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ERequestStatus status;

    @Column(name = "requestDate", nullable = false)
    private LocalDateTime requestDate;

    public void addEvent(EventEntity event) {
        event.addEventRequest(this);
    }

    public void removeEvent(EventEntity event) {
        event.removeEventRequest(this);
    }

    public void addUser(UserEntity user) {
        user.addEventRequests(this);
    }

    public void removeUser(UserEntity user) {
        user.removeEventRequests(this);
    }
}
