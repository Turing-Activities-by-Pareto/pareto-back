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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "event")
@Builder
public class EventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;

    private String minioBucket;
    private String objectName;

    @ManyToOne
    @JoinColumn(name = "category")
    private EventCategoryEntity category;

    @ManyToOne
    @JoinColumn(name = "sub_category")
    private SubEventCategoryEntity subCategory;

    private String place;

    @ElementCollection(targetClass = EParticipantCategory.class, fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)  // Store enum as string in DB
    @CollectionTable(name = "event_allowed_categories",
            joinColumns = @JoinColumn(name = "event_id"))
    @Column(name = "participant_category")
    private List<EParticipantCategory> participantCategories;

    private LocalDateTime deadline;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @Enumerated(EnumType.STRING)
    private EConfirmStatus confirmStatus;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private List<EventRequestEntity> requests;

    @OneToOne
    @MapsId
    private FileEntity file;
}
