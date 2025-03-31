package com.pareto.activities.service;

import com.pareto.activities.DTO.UserCreateRequest;
import com.pareto.activities.DTO.UserResponse;
import com.pareto.activities.entity.UserEntity;
import com.pareto.activities.enums.BusinessStatus;
import com.pareto.activities.exception.BusinessException;
import com.pareto.activities.mapper.UserMapper;
import com.pareto.activities.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    public void createUser(UserCreateRequest user) {

        UserEntity userEntity = UserEntity.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .role(user.getRole())
                .build()
                ;

        userRepository.save(userEntity);

        log.info("User created: {}", userEntity);
    }

    public List<UserResponse> retrieveUsers() {

        List<UserEntity> users = userRepository.findAll();

        log.info("Users retrieved: {}", users);

        return users.stream()
                .map(userMapper::toUserResponse)
                .toList();
    }

    public UserResponse getUserByUsername(String username) {

        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException(BusinessStatus.USER_NOT_FOUND, HttpStatus.NOT_FOUND));

        log.info("User retrieved: {}", user);

        return userMapper.toUserResponse(user);
    }

    @Transactional
    public UserResponse updateUser(
            String username,
            UserCreateRequest user
    ) {

        UserEntity existingUserEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException(BusinessStatus.USER_NOT_FOUND, HttpStatus.NOT_FOUND));

        UserEntity updatedUserEntity = userMapper.toUserEntity(user);
        updatedUserEntity.setId(existingUserEntity.getId());

        userRepository.save(updatedUserEntity);

        log.info("User updated: {}", updatedUserEntity);

        return userMapper.toUserResponse(updatedUserEntity);
    }


    @Transactional
    public void deleteUser(String username) {

        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException(BusinessStatus.USER_NOT_FOUND, HttpStatus.NOT_FOUND));

        userRepository.delete(user);

        log.info("User deleted: {}", user);
    }
}
