package com.nexturn.lms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nexturn.lms.entity.User;
import com.nexturn.lms.exception.InvalidCredentialsException;
import com.nexturn.lms.exception.InvalidRequestException;
import com.nexturn.lms.exception.ResourceNotFoundException;
import com.nexturn.lms.repository.UserRepository;
import com.nexturn.lms.utils.Role;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User register(String firstName, String lastName, String email, String rawPassword, Role role) {
        userRepository.findByEmail(email).ifPresent(u -> {
            throw new InvalidRequestException("Email already registered: " + email);
        });

        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(rawPassword); // TODO: replace with passwordEncoder.encode(rawPassword) once Security is added
        user.setRole(role);

        return userRepository.save(user);
    }

    @Override
    public User login(String email, String rawPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        if (!user.getPassword().equals(rawPassword)) { 
            throw new InvalidCredentialsException("Invalid email or password");
        }
        return user;
    }

    @Override
    public User getById(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));
    }
}