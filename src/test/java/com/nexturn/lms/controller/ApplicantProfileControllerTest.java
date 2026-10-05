package com.nexturn.lms.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.nexturn.lms.entity.ApplicantProfile;
import com.nexturn.lms.entity.User;
import com.nexturn.lms.service.ApplicantProfileService;
import com.nexturn.lms.service.UserService;

@ExtendWith(MockitoExtension.class)
class ApplicantProfileControllerTest {

    @Mock
    private ApplicantProfileService profileService;

    @Mock
    private UserService userService;

    @InjectMocks
    private ApplicantProfileController applicantProfileController;

    private MockMvc mockMvc;

    private User user;
    private ApplicantProfile profile;

    @BeforeEach
    void setUp() {

        mockMvc = MockMvcBuilders
                .standaloneSetup(applicantProfileController)
                .build();

        user = new User();
        user.setUserId(1);

        profile = new ApplicantProfile();
        profile.setProfileId(1);
        profile.setUser(user);
    }

    @Test
    void createOrUpdate_shouldReturnProfile() throws Exception {

        when(userService.getById(1))
                .thenReturn(user);

        when(profileService.createOrUpdateProfile(
                org.mockito.ArgumentMatchers.eq(user),
                org.mockito.ArgumentMatchers.any(LocalDate.class),
                org.mockito.ArgumentMatchers.eq("Hyderabad"),
                org.mockito.ArgumentMatchers.eq("ABCDE1234F"),
                org.mockito.ArgumentMatchers.eq("123456789012"),
                org.mockito.ArgumentMatchers.eq("SALARIED"),
                org.mockito.ArgumentMatchers.eq(new BigDecimal("50000")),
                org.mockito.ArgumentMatchers.eq(new BigDecimal("5000"))
        )).thenReturn(profile);

        String requestBody = """
                {
                    "dateOfBirth": "1998-01-15",
                    "address": "Hyderabad",
                    "panNumber": "ABCDE1234F",
                    "aadhaarNumber": "123456789012",
                    "employmentType": "SALARIED",
                    "monthlyIncome": 50000,
                    "existingLiabilities": 5000
                }
                """;

        mockMvc.perform(
                post("/api/applicant/profile/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        )
        .andExpect(status().isOk());

        verify(userService).getById(1);

        verify(profileService).createOrUpdateProfile(
                org.mockito.ArgumentMatchers.eq(user),
                org.mockito.ArgumentMatchers.any(LocalDate.class),
                org.mockito.ArgumentMatchers.eq("Hyderabad"),
                org.mockito.ArgumentMatchers.eq("ABCDE1234F"),
                org.mockito.ArgumentMatchers.eq("123456789012"),
                org.mockito.ArgumentMatchers.eq("SALARIED"),
                org.mockito.ArgumentMatchers.eq(new BigDecimal("50000")),
                org.mockito.ArgumentMatchers.eq(new BigDecimal("5000"))
        );
    }

    @Test
    void getByUser_shouldReturnProfile() throws Exception {

        when(userService.getById(1))
                .thenReturn(user);

        when(profileService.getByUser(user))
                .thenReturn(profile);

        mockMvc.perform(
                get("/api/applicant/profile/1")
        )
        .andExpect(status().isOk());

        verify(userService).getById(1);

        verify(profileService).getByUser(user);
    }
}