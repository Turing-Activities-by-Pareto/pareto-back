package com.pareto.activities.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
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

    @Column(name = "name", nullable = false)
    private String name;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private FileEntity file;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EventSubCategoryEntity> subCategories;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EventEntity> events;

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

    //helper methods for events
    public void addEvent(EventEntity event) {
        events.add(event);
        event.setCategory(this);
    }

    public void removeEvent(EventEntity event) {
        events.remove(event);
        event.setCategory(null);
    }
}
