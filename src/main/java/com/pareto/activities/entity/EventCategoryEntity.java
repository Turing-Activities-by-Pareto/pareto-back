package com.pareto.activities.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventCategoryEntity {

    @Id
    private String id;
    private String name;
    private FileEntity file;

    private Set<String> eventIds;
}
