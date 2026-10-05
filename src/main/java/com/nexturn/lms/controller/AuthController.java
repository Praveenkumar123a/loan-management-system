package com.nexturn.lms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.nexturn.lms.dto.LoginRequest;
import com.nexturn.lms.dto.RegisterRequest;
import com.nexturn.lms.dto.UserResponse;
import com.nexturn.lms.entity.User;
import com.nexturn.lms.service.UserService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody RegisterRequest request) {
        User user = userService.register(
                request.getFirstName(), request.getLastName(),
                request.getEmail(), request.getPassword(), request.getRole());
        return ResponseEntity.ok(new UserResponse(user));
    }
    
    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@RequestBody LoginRequest request) {
        User user = userService.login(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(new UserResponse(user));
    }
}