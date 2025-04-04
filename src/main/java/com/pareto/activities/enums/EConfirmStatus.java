package com.pareto.activities.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum EConfirmStatus {
    CONFIRMED,
    REJECTED,
    PENDING,
    ACTIVE,
    INACTIVE;

    @JsonCreator
    public static EConfirmStatus fromValue(String status) {
        return Arrays
                .stream(EConfirmStatus.values())
                .filter(e -> e
                        .name()
                        .equalsIgnoreCase(status))
                .findFirst()
                .orElse(null);
    }

    @JsonValue
    public String toValue() {
        return name().toUpperCase();
    }
}
