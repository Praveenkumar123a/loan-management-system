package com.nexturn.lms.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.nexturn.lms.entity.User;
import com.nexturn.lms.service.UserService;
import com.nexturn.lms.utils.Role;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthController authController;

    private MockMvc mockMvc;

    private User user;

    @BeforeEach
    void setUp() {

        mockMvc = MockMvcBuilders
                .standaloneSetup(authController)
                .build();

        user = new User();
        user.setUserId(1);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john@example.com");
        user.setRole(Role.APPLICANT);
    }

    @Test
    void register_shouldReturnUser() throws Exception {

        when(userService.register(
                "John",
                "Doe",
                "john@example.com",
                "password123",
                Role.APPLICANT
        )).thenReturn(user);

        String requestBody = """
                {
                    "firstName": "John",
                    "lastName": "Doe",
                    "email": "john@example.com",
                    "password": "password123",
                    "role": "APPLICANT"
                }
                """;

        mockMvc.perform(
                post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        )
        .andExpect(status().isOk());

        verify(userService).register(
                "John",
                "Doe",
                "john@example.com",
                "password123",
                Role.APPLICANT
        );
    }

    @Test
    void login_shouldReturnUser() throws Exception {

        when(userService.login(
                "john@example.com",
                "password123"
        )).thenReturn(user);

        String requestBody = """
                {
                    "email": "john@example.com",
                    "password": "password123"
                }
                """;

        mockMvc.perform(
                post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        )
        .andExpect(status().isOk());

        verify(userService).login(
                "john@example.com",
                "password123"
        );
    }
}