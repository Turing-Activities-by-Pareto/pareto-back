package com.pareto.activities.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum ERequestStatus {
    PENDING,
    APPROVED,
    DECLINED,
    ;

    @JsonValue
    public static String toValue(ERequestStatus status) {
        return status.name();
    }

    @JsonCreator
    public static ERequestStatus fromValue(String status) {
        return Arrays
                .stream(ERequestStatus.values())
                .filter(e -> e
                        .name()
                        .equalsIgnoreCase(status))
                .findFirst()
                .orElse(null);
    }
}
