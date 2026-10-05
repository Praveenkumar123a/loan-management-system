package com.nexturn.lms.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.nexturn.lms.entity.User;
import com.nexturn.lms.exception.InvalidCredentialsException;
import com.nexturn.lms.exception.InvalidRequestException;
import com.nexturn.lms.exception.ResourceNotFoundException;
import com.nexturn.lms.repository.UserRepository;
import com.nexturn.lms.utils.Role;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    @BeforeEach
    void setUp() {

        user = new User();

        user.setUserId(1);
        user.setFirstName("Punit");
        user.setLastName("Kumar");
        user.setEmail("punit@example.com");
        user.setPassword("password123");
        user.setRole(Role.APPLICANT);
    }

    @Test
    void register_shouldCreateUser() {

        when(userRepository.findByEmail("punit@example.com"))
                .thenReturn(Optional.empty());

        when(userRepository.save(org.mockito.ArgumentMatchers.any(User.class)))
                .thenReturn(user);

        User result = userService.register(
                "Punit",
                "Kumar",
                "punit@example.com",
                "password123",
                Role.APPLICANT
        );

        assertEquals("Punit", result.getFirstName());
        assertEquals("Kumar", result.getLastName());
        assertEquals("punit@example.com", result.getEmail());
        assertEquals(Role.APPLICANT, result.getRole());

        verify(userRepository).findByEmail("punit@example.com");
        verify(userRepository).save(org.mockito.ArgumentMatchers.any(User.class));
    }

    @Test
    void register_shouldThrowExceptionWhenEmailAlreadyExists() {

        when(userRepository.findByEmail("punit@example.com"))
                .thenReturn(Optional.of(user));

        assertThrows(
                InvalidRequestException.class,
                () -> userService.register(
                        "Punit",
                        "Kumar",
                        "punit@example.com",
                        "password123",
                        Role.APPLICANT
                )
        );
    }

    @Test
    void login_shouldReturnUserWithCorrectPassword() {

        when(userRepository.findByEmail("punit@example.com"))
                .thenReturn(Optional.of(user));

        User result = userService.login(
                "punit@example.com",
                "password123"
        );

        assertEquals(
                user.getUserId(),
                result.getUserId()
        );

        assertEquals(
                "punit@example.com",
                result.getEmail()
        );
    }

    @Test
    void login_shouldThrowExceptionWhenEmailDoesNotExist() {

        when(userRepository.findByEmail("unknown@example.com"))
                .thenReturn(Optional.empty());

        assertThrows(
                InvalidCredentialsException.class,
                () -> userService.login(
                        "unknown@example.com",
                        "password123"
                )
        );
    }

    @Test
    void login_shouldThrowExceptionWhenPasswordIsWrong() {

        when(userRepository.findByEmail("punit@example.com"))
                .thenReturn(Optional.of(user));

        assertThrows(
                InvalidCredentialsException.class,
                () -> userService.login(
                        "punit@example.com",
                        "wrongPassword"
                )
        );
    }

    @Test
    void getById_shouldReturnUser() {

        when(userRepository.findById(1))
                .thenReturn(Optional.of(user));

        User result = userService.getById(1);

        assertEquals(1, result.getUserId());
        assertEquals("punit@example.com", result.getEmail());

        verify(userRepository).findById(1);
    }

    @Test
    void getById_shouldThrowExceptionWhenUserDoesNotExist() {

        when(userRepository.findById(999))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> userService.getById(999)
        );
    }
}