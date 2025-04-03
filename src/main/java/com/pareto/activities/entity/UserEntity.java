package com.pareto.activities.entity;

import com.pareto.activities.enums.EParticipantCategory;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "_user")
@ToString
public class UserEntity {

    //TODO implement soft delete (disabled user)

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private long id;
    private String username;
    private String password;

    @Enumerated(EnumType.STRING)
    private EParticipantCategory role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EventEntity> events;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EventRequestEntity> eventRequests;

    //helper methods for events
    public void addEvent(EventEntity event) {
        events.add(event);
        event.setUser(this);
    }

    public void removeEvent(EventEntity event) {
        events.remove(event);
        event.setUser(null);
    }

    //helper methods for eventRequest
    public void addEventRequests(EventRequestEntity eventRequest) {
        eventRequests.add(eventRequest);
        eventRequest.setUser(this);
    }

    public void removeEventRequests(EventRequestEntity eventRequest) {
        eventRequests.remove(eventRequest);
        eventRequest.setUser(null);
    }
}