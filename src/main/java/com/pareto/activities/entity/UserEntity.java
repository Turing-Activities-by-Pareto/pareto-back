package com.pareto.activities.entity;

import com.pareto.activities.enums.EParticipantCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Set;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserEntity {

    @Id
    private String id;
    private String username;
    private String password;

    @Field(name = "role")
    private EParticipantCategory role;

    private boolean isActive;

    @DBRef
    private Set<EventEntity> events;

    @DBRef
    private Set<EventRequestEntity> eventRequests;
}