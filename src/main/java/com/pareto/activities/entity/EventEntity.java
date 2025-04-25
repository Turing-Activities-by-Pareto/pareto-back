package com.pareto.activities.entity;

import com.pareto.activities.enums.EConfirmStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "event")
public class EventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_category_id", nullable = false)
    private EventCategoryEntity category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_sub_category_id", nullable = false)
    private EventSubCategoryEntity subCategory;

    //is used for checking whether the event is in Turing or outside of Turing (Turingdaxili, Turingxarici)
    @Column(name = "is_local", nullable = false)
    private Boolean isLocal;

    //will be assigned a string value by checking isLocal field
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Location location;

    @ManyToMany(
            fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            }
    )
    @JoinTable(
            name = "event_participant_category",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "participant_category_id")
    )
    private Set<ParticipantCategory> participantCategories;

    @Column(name = "deadline", nullable = false)
    private LocalDateTime deadline;

    @Column(name = "startDate", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "endDate", nullable = false)
    private LocalDateTime endDate;

    //limitless participant feature
    @Column(name = "unlimited_seats", nullable = false)
    private Boolean unlimitedSeats;

    @Column(name = "remaining_seats", nullable = false)
    private Integer remainingSeats;

    @Enumerated(EnumType.STRING)
    @Column(name = "confirmStatus", nullable = true)
    private EConfirmStatus confirmStatus;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EventRequestEntity> requests;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id")
    private FileEntity file;

    //will be used in Event Request logic
    public boolean hasAvailableSeat() {
        return unlimitedSeats || remainingSeats > 0;
    }

    //will be used in Event Request logic
    public void decreaseSeat() {
        if (!unlimitedSeats && remainingSeats > 0) {
            remainingSeats--;
        }
    }

    //helper methods
    public void setLocation(Location location) {
        if (location == null) {
            if (this.location != null) {
                this.location.setEvent(null);
            }
        } else {
            location.setEvent(this);
        }
        this.location = location;
    }

    public void addParticipantCategory(ParticipantCategory category) {
        participantCategories.add(category);
        category.getEvents().add(this);
    }

    public void removeParticipantCategory(ParticipantCategory category) {
        participantCategories.remove(category);
        category.getEvents().remove(this);
    }

    public void addAllParticipantCategories(Set<ParticipantCategory> categories) {
        for (ParticipantCategory participantCategory : categories) {
            addParticipantCategory(participantCategory);
        }
    }

    public void removeAllParticipantCategories(Set<ParticipantCategory> categories) {
        for (ParticipantCategory participantCategory : categories) {
            removeParticipantCategory(participantCategory);
        }
    }

    public void addEventRequest(EventRequestEntity request) {
        requests.add(request);
        request.setEvent(this);
    }

    public void removeEventRequest(EventRequestEntity request) {
        requests.remove(request);
        request.setEvent(null);
    }

    public void addUser(UserEntity user) {
        user.addEvent(this);
    }

    public void removeUser(UserEntity user) {
        user.removeEvent(this);
    }

    public void addCategory(EventCategoryEntity category) {
        category.addEventEntity(this);
    }

    public void removeCategory(EventCategoryEntity category) {
        category.removeEventEntity(this);
    }

    public void addSubCategory(EventSubCategoryEntity subCategory) {
        subCategory.addEvent(this);
    }

    public void removeSubCategory(EventSubCategoryEntity subCategory) {
        subCategory.removeEvent(this);
    }
}
