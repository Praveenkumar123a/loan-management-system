package com.nexturn.lms.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nexturn.lms.entity.User;
import com.nexturn.lms.exception.UserNotFoundException;
import com.nexturn.lms.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository repo;

    @Override
    public String addUser(User user) {
        User u = repo.save(user);
        String str = "User inserted " + u.getUserId();
        return str;
    }

    @Override
    public String updateUser(User user) {
        repo.save(user);
        String str = "User updated";
        return str;
    }

    @Override
    public String removeUser(Long userId) {
        repo.deleteById(userId);
        return "User deleted";
    }

    @Override
    public List<User> findAllUsers() {
        return repo.findAll();
    }

    @Override
    public User findUserById(Long userId) {
        Optional<User> user = repo.findById(userId);

        if (user.isEmpty())
            throw new UserNotFoundException();

        return user.get();
    }

    @Override
    public User findUserByEmail(String email) {
        Optional<User> user = repo.findByEmail(email);

        if (user.isEmpty())
            throw new UserNotFoundException();

        return user.get();
    }
}