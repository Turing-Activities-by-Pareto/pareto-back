package com.pareto.activities.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.pareto.activities.exception.BusinessException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

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
    public String getDisplayName() {
        return name().toUpperCase();
    }

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
                .orElseThrow(() -> new BusinessException(
                        "Invalid participant category: " + status,
                        BusinessStatus.UNDEFINED_PARTICIPANT_CATEGORY,
                        HttpStatus.BAD_REQUEST
                ))
                ;
    }
}
