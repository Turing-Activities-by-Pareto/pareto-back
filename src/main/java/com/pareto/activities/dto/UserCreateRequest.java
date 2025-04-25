package com.pareto.activities.dto;

import com.pareto.activities.enums.EParticipantCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateRequest {
    //implement validations here
    private String username;
    private String password;
    private EParticipantCategory role;
}
