package com.nexturn.lms.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.nexturn.lms.entity.ApplicantProfile;
import com.nexturn.lms.entity.User;
import com.nexturn.lms.exception.ResourceNotFoundException;
import com.nexturn.lms.repository.ApplicantProfileRepository;
import com.nexturn.lms.utils.Role;

@ExtendWith(MockitoExtension.class)
class ApplicantProfileServiceImplTest {

    @Mock
    private ApplicantProfileRepository profileRepository;

    @InjectMocks
    private ApplicantProfileServiceImpl profileService;

    private User user;
    private ApplicantProfile profile;

    @BeforeEach
    void setUp() {

        user = new User();

        user.setUserId(1);
        user.setFirstName("Punit");
        user.setLastName("Kumar");
        user.setEmail("punit@example.com");
        user.setPassword("password123");
        user.setRole(Role.APPLICANT);

        profile = new ApplicantProfile();

        profile.setProfileId(1);
        profile.setUser(user);
        profile.setDateOfBirth(
                LocalDate.of(2000, 1, 1)
        );
        profile.setAddress("Hyderabad");
        profile.setPanNumber("ABCDE1234F");
        profile.setAadhaarNumber("123456789012");
        profile.setEmploymentType("SALARIED");
        profile.setMonthlyIncome(
                new BigDecimal("50000")
        );
        profile.setExistingLiabilities(
                new BigDecimal("10000")
        );
    }

    @Test
    void createOrUpdateProfile_shouldCreateNewProfile() {

        when(profileRepository.findByUser(user))
                .thenReturn(Optional.empty());

        when(profileRepository.save(any(ApplicantProfile.class)))
                .thenReturn(profile);

        ApplicantProfile result =
                profileService.createOrUpdateProfile(
                        user,
                        LocalDate.of(2000, 1, 1),
                        "Hyderabad",
                        "ABCDE1234F",
                        "123456789012",
                        "SALARIED",
                        new BigDecimal("50000"),
                        new BigDecimal("10000")
                );

        assertNotNull(result);

        assertEquals(
                user,
                result.getUser()
        );

        assertEquals(
                LocalDate.of(2000, 1, 1),
                result.getDateOfBirth()
        );

        assertEquals(
                "Hyderabad",
                result.getAddress()
        );

        assertEquals(
                "ABCDE1234F",
                result.getPanNumber()
        );

        assertEquals(
                "123456789012",
                result.getAadhaarNumber()
        );

        assertEquals(
                "SALARIED",
                result.getEmploymentType()
        );

        assertEquals(
                new BigDecimal("50000"),
                result.getMonthlyIncome()
        );

        assertEquals(
                new BigDecimal("10000"),
                result.getExistingLiabilities()
        );

        verify(profileRepository)
                .findByUser(user);

        verify(profileRepository)
                .save(any(ApplicantProfile.class));
    }

    @Test
    void createOrUpdateProfile_shouldUpdateExistingProfile() {

        ApplicantProfile existingProfile =
                new ApplicantProfile();

        existingProfile.setProfileId(1);
        existingProfile.setUser(user);

        when(profileRepository.findByUser(user))
                .thenReturn(Optional.of(existingProfile));

        when(profileRepository.save(existingProfile))
                .thenReturn(existingProfile);

        ApplicantProfile result =
                profileService.createOrUpdateProfile(
                        user,
                        LocalDate.of(1999, 5, 10),
                        "Bangalore",
                        "FGHIJ5678K",
                        "987654321098",
                        "SELF_EMPLOYED",
                        new BigDecimal("75000"),
                        new BigDecimal("15000")
                );

        assertEquals(
                existingProfile,
                result
        );

        assertEquals(
                user,
                result.getUser()
        );

        assertEquals(
                LocalDate.of(1999, 5, 10),
                result.getDateOfBirth()
        );

        assertEquals(
                "Bangalore",
                result.getAddress()
        );

        assertEquals(
                "FGHIJ5678K",
                result.getPanNumber()
        );

        assertEquals(
                "987654321098",
                result.getAadhaarNumber()
        );

        assertEquals(
                "SELF_EMPLOYED",
                result.getEmploymentType()
        );

        assertEquals(
                new BigDecimal("75000"),
                result.getMonthlyIncome()
        );

        assertEquals(
                new BigDecimal("15000"),
                result.getExistingLiabilities()
        );

        verify(profileRepository)
                .findByUser(user);

        verify(profileRepository)
                .save(existingProfile);
    }

    @Test
    void createOrUpdateProfile_shouldSetZeroWhenExistingLiabilitiesIsNull() {

        when(profileRepository.findByUser(user))
                .thenReturn(Optional.empty());

        when(profileRepository.save(any(ApplicantProfile.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0));

        ApplicantProfile result =
                profileService.createOrUpdateProfile(
                        user,
                        LocalDate.of(2000, 1, 1),
                        "Hyderabad",
                        "ABCDE1234F",
                        "123456789012",
                        "SALARIED",
                        new BigDecimal("50000"),
                        null
                );

        assertEquals(
                BigDecimal.ZERO,
                result.getExistingLiabilities()
        );

        verify(profileRepository)
                .save(any(ApplicantProfile.class));
    }

    @Test
    void getByUser_shouldReturnProfile() {

        when(profileRepository.findByUser(user))
                .thenReturn(Optional.of(profile));

        ApplicantProfile result =
                profileService.getByUser(user);

        assertNotNull(result);

        assertEquals(
                1,
                result.getProfileId()
        );

        assertEquals(
                user,
                result.getUser()
        );

        assertEquals(
                "Hyderabad",
                result.getAddress()
        );

        verify(profileRepository)
                .findByUser(user);
    }

    @Test
    void getByUser_shouldThrowExceptionWhenProfileDoesNotExist() {

        when(profileRepository.findByUser(user))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> profileService.getByUser(user)
        );

        verify(profileRepository)
                .findByUser(user);
    }
}