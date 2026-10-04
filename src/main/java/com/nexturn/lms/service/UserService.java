package com.nexturn.lms.service;

import java.util.List;
import java.util.Optional;

import com.nexturn.lms.entity.User;

public interface UserService {

    User createUser(User user);

    List<User> getAllUsers();

    Optional<User> getUserById(Integer userId);

    Optional<User> getUserByEmail(String email);

    User updateUser(Integer userId, User user);

    void deleteUser(Integer userId);
}