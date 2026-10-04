package com.nexturn.lms.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nexturn.lms.entity.User;
import com.nexturn.lms.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> createUser(
            @RequestBody User user) {

        User savedUser = userService.createUser(user);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedUser);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {

        return ResponseEntity.ok(
                userService.getAllUsers());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(
            @PathVariable Integer userId) {

        return userService.getUserById(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(
            @PathVariable String email) {

        return userService.getUserByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(
            @PathVariable Integer userId,
            @RequestBody User user) {

        return ResponseEntity.ok(
                userService.updateUser(userId, user));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable Integer userId) {

        userService.deleteUser(userId);

        return ResponseEntity.noContent().build();
    }
}