package com.nexturn.lms.service;

import com.nexturn.lms.entity.User;
import com.nexturn.lms.utils.Role;

public interface UserService {
    User register(String firstName, String lastName, String email, String rawPassword, Role role);
    User login(String email, String rawPassword);
    User getById(Integer userId);
}