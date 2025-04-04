package com.pareto.activities.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.pareto.activities.exception.BusinessException;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

public enum ERequestStatus {
    PENDING,
    APPROVED,
    DECLINED,
    ;

    public String fromName() {
        return name().toUpperCase();
    }

    public String toDisplayName() {
        return name().toUpperCase();
    }

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
                .orElseThrow(
                        () -> new BusinessException(
                                "Invalid request status: " + status,
                                BusinessStatus.UNDEFINED_REQUEST_STATUS,
                                HttpStatus.BAD_REQUEST
                        )
                );
    }
}
