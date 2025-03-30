package com.pareto.activities.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EEventCategory {
    STUDENT("tələbə"),
    TEACHER("müəllim"),
    MENTOR("mentor"),
    COMMUNITY_STAFF("community əməkdaşları"),
    GUEST("qonaq"),

    ;

    private final String translation;

}
