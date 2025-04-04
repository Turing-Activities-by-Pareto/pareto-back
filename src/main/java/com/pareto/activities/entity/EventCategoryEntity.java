package com.pareto.activities.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Document
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
public class EventCategoryEntity {

    @Id
    @EqualsAndHashCode.Include
    private String id;
    private String name;
    private FileEntity file;

    @DBRef
    private Set<EventSubCategoryEntity> subCategories;

    @DBRef
    private Set<EventEntity> events;
}
