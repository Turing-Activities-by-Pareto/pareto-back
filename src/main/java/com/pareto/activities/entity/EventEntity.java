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
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
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
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(
        name = "event",
        indexes = {
                @Index(name = "idx_category", columnList = "category"),
                @Index(name = "idx_sub_category", columnList = "subCategory"),
                @Index(name = "idx_subCategory", columnList = "subCategory")
        }
)
public class EventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = true)
    private UserEntity user;

//    @Column(name = "title", nullable = true)
    private String title;

//    @Column(name = "description", nullable = true)
    private String description;

    private String category;
    private String subCategory;

//    @Column(name = "place", nullable = true)
    private String place;

    @ElementCollection(targetClass = EParticipantCategory.class, fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(
            name = "event_allowed_categories",
            joinColumns = @JoinColumn(name = "event_id")
    )
    @Column(name = "participant_category")
    private List<EParticipantCategory> participantCategories;

    @Column(name = "deadline", nullable = true)
    private LocalDateTime deadline;

    @Column(name = "startDate", nullable = true)
    private LocalDateTime startDate;

    @Column(name = "endDate", nullable = true)
    private LocalDateTime endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "confirmStatus", nullable = true)
    private EConfirmStatus confirmStatus;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EventRequestEntity> requests;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id")
    private FileEntity file;
}
