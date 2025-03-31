package com.pareto.activities.mapper;

import com.pareto.activities.DTO.UserCreateRequest;
import com.pareto.activities.DTO.UserResponse;
import com.pareto.activities.entity.UserEntity;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")
@Component
public interface UserMapper {

    UserResponse toUserResponse(UserEntity userEntity);

    List<UserResponse> toUserResponseList(List<UserEntity> userEntities);

    UserCreateRequest toUserCreateRequest(UserEntity userEntity);

    UserEntity toUserEntity(UserCreateRequest userCreateRequest);
}