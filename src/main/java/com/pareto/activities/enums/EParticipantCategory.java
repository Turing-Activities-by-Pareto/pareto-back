package com.pareto.activities.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
//will be named as 'role'
public enum EParticipantCategory {
    STUDENT,
    TEACHER,
    MENTOR,
    COMMUNITY_STAFF,
    GUEST,

}
