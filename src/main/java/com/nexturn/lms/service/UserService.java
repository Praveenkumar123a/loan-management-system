package com.nexturn.lms.service;

import java.util.List;
import java.util.Optional;

import com.nexturn.lms.entity.User;

public interface UserService {

    User createUser(User user);
    List<User> getAllUsers();
    Optional<User> getUserById(Long userId);
    Optional<User> getUserByEmail(String email);
    User updateUser(Long userId, User user);
    void deleteUser(Long userId);
}