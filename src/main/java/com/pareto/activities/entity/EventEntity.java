package com.pareto.activities.entity;

import com.pareto.activities.enums.EConfirmStatus;
import com.pareto.activities.enums.EParticipantCategory;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
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
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "event")
@Builder
public class EventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    private String title;
    private String description;

    @Getter(AccessLevel.NONE)
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "event_event_category",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "event_category_id")
    )
    private Set<EventCategoryEntity> categories;

    @Getter(AccessLevel.NONE)
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "event_event_sub_category",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "event_sub_category_id")
    )
    private List<EventSubCategoryEntity> subCategories;

    private String place;

    @ElementCollection(targetClass = EParticipantCategory.class, fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    // Store enum as string in DB
    @CollectionTable(name = "event_allowed_categories",
            joinColumns = @JoinColumn(name = "event_id"))
    @Column(name = "participant_category")
    private List<EParticipantCategory> participantCategories;

    private LocalDateTime deadline;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @Enumerated(EnumType.STRING)
    private EConfirmStatus confirmStatus;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EventRequestEntity> requests;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id")
    private FileEntity file;

    //helper methods for requests
    public void addRequest(EventRequestEntity request) {
        requests.add(request);
        request.setEvent(this);
    }

    public void removeRequest(EventRequestEntity request) {
        requests.remove(request);
        request.setEvent(null);
    }

    //helper methods for categories
    public void addCategory(EventCategoryEntity category) {
        categories.add(category);
        category.getEvents().add(this);
    }

    public void removeCategory(EventCategoryEntity category) {
        categories.remove(category);
        category.getEvents().remove(this);
    }

    //helper methods for subCategories
    public void addSubCategory(EventSubCategoryEntity subCategory) {
        subCategories.add(subCategory);
        subCategory.getEvents().add(this);
    }

    public void removeSubCategory(EventSubCategoryEntity subCategory) {
        subCategories.remove(subCategory);
        subCategory.getEvents().remove(this);
    }
}
