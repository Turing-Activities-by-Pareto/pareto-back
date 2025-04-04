package com.pareto.activities.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum EParticipantCategory {
    STUDENT,
    TEACHER,
    MENTOR,
    COMMUNITY_STAFF,
    GUEST,
    ;

    @JsonValue
    public static String toValue(EParticipantCategory status) {
        return status
                .name()
                .toUpperCase();
    }

    @JsonCreator
    public static EParticipantCategory fromValue(String status) {
        return Arrays
                .stream(EParticipantCategory.values())
                .filter(e -> e
                        .name()
                        .equalsIgnoreCase(status))
                .findFirst()
                .orElse(null);
    }
}
