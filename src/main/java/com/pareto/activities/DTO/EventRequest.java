package com.pareto.activities.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pareto.activities.enums.EEventCategory;
import com.pareto.activities.enums.EParticipantCategory;
import com.pareto.activities.enums.EEventSubCategory;

import java.util.List;

public class EventRequest {
    public String name;
    public String description;
    public String place;

    @JsonProperty("participant_allowance")
    public List<EParticipantCategory> participantAllowance;

    public EEventCategory category;

    @JsonProperty("sub_category")
    public EEventSubCategory subCategory;
}
