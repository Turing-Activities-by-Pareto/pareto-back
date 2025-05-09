package com.pareto.activities.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "event_category")
public class EventCategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private FileEntity file;

    @Setter(AccessLevel.NONE)
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EventSubCategoryEntity> subCategories;

    @Setter(AccessLevel.NONE)
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EventEntity> events;

    public void addEventEntity(EventEntity event) {
        events.add(event);
        event.setCategory(this);
    }

    public void removeEventEntity(EventEntity event) {
        events.remove(event);
        event.setCategory(null);
    }

    //helper methods for subCategories
    public void addSubCategory(EventSubCategoryEntity subCategory) {
        subCategories.add(subCategory);
        subCategory.setCategory(this);
    }

    public void removeSubCategory(EventSubCategoryEntity subCategory) {
        subCategories.remove(subCategory);
        subCategory.setCategory(null);
    }

    public void addAllSubCategory(List<EventSubCategoryEntity> subCategories) {
        for (EventSubCategoryEntity subCategory : subCategories) {
            this.addSubCategory(subCategory);
        }
    }
}
