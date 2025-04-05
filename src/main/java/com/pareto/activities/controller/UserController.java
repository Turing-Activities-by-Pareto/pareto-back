package com.pareto.activities.controller;

import com.pareto.activities.DTO.UserCreateRequest;
import com.pareto.activities.DTO.UserResponse;
import com.pareto.activities.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userServise;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse createUser(
            @RequestBody UserCreateRequest user
    ) {
        return userServise.createUser(user);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserResponse> retrieveUsers() {
        return userServise.retrieveUsers();
    }

    @GetMapping("/{username}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse getUserByUsername(@PathVariable String username) {
        return userServise.getUserByUsername(username);
    }

    @PutMapping("/{username}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse updateUser(
            @PathVariable String username,
            @RequestBody UserCreateRequest user
    ) {
        return userServise.updateUser(
                username,
                user
        );
    }

    @DeleteMapping("/{username}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable String username) {
        userServise.deleteUser(username);
    }
}
