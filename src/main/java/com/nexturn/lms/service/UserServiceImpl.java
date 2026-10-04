package com.nexturn.lms.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.nexturn.lms.entity.User;
import com.nexturn.lms.exception.UserNotFoundException;
import com.nexturn.lms.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(User user) {

        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {

        return userRepository.findAll();
    }

    @Override
    public Optional<User> getUserById(Integer userId) {

        return userRepository.findById(userId);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {

        return userRepository.findByEmail(email);
    }

    @Override
    public User updateUser(Integer userId, User user) {

        User existingUser = userRepository.findById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found"));

        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        existingUser.setEmail(user.getEmail());
        existingUser.setPassword(user.getPassword());
        existingUser.setRole(user.getRole());

        return userRepository.save(existingUser);
    }

    @Override
    public void deleteUser(Integer userId) {

        User existingUser = userRepository.findById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found"));

        userRepository.delete(existingUser);
    }
}