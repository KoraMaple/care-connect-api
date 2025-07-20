package com.careconnect.coreapi.childmgmt.service;

import com.careconnect.coreapi.childmgmt.domain.Guardian;
import com.careconnect.coreapi.childmgmt.internal.repository.GuardianRepository;
import com.careconnect.coreapi.childmgmt.internal.service.GuardianService;
import com.careconnect.coreapi.common.exceptions.ResourceNotFoundException;
import com.careconnect.coreapi.common.exceptions.ValidationException;
import com.careconnect.coreapi.user.UserService;
import com.careconnect.coreapi.user.domain.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GuardianServiceTest {

    @Mock
    private GuardianRepository guardianRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private GuardianService guardianService;

    private Guardian testGuardian;
    private User testUser;
    private UUID guardianId;
    private UUID userId;

    @BeforeEach
    void setUp() {
        guardianId = UUID.randomUUID();
        userId = UUID.randomUUID();

        testUser = new User();
        testUser.setId(userId);
        testUser.setClerkUserId("user_123");

        testGuardian = new Guardian();
        testGuardian.setId(guardianId);
        testGuardian.setUserId(userId);
        testGuardian.setRelationship("Parent");
        testGuardian.setEmergencyContact("Primary emergency contact");
        testGuardian.setPickupAuthorized(true);
        testGuardian.setCreatedAt(Instant.now());
        testGuardian.setUpdatedAt(Instant.now());
    }

    @Test
    void getAllGuardians_ShouldReturnAllGuardians() {
        // Given
        when(guardianRepository.findAll()).thenReturn(List.of(testGuardian));

        // When
        List<Guardian> result = guardianService.getAllGuardians();

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getRelationship()).isEqualTo("Parent");
        verify(guardianRepository).findAll();
    }

    @Test
    void getGuardianById_WhenGuardianExists_ShouldReturnGuardian() {
        // Given
        when(guardianRepository.findById(guardianId)).thenReturn(Optional.of(testGuardian));

        // When
        Guardian result = guardianService.getGuardianById(guardianId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getRelationship()).isEqualTo("Parent");
        verify(guardianRepository).findById(guardianId);
    }

    @Test
    void getGuardianById_WhenGuardianDoesNotExist_ShouldThrowResourceNotFoundException() {
        // Given
        when(guardianRepository.findById(guardianId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> guardianService.getGuardianById(guardianId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Guardian not found with ID: " + guardianId);
        verify(guardianRepository).findById(guardianId);
    }

    @Test
    void createGuardian_WithValidData_ShouldCreateGuardian() {
        // Given
        Guardian newGuardian = new Guardian();
        newGuardian.setUserId(userId);
        newGuardian.setRelationship("Parent");
        newGuardian.setEmergencyContact("Primary contact");

        when(userService.findUserEntityById(userId)).thenReturn(Optional.of(testUser));
        when(guardianRepository.findByUserId(userId)).thenReturn(Optional.empty());
        when(guardianRepository.save(any(Guardian.class))).thenReturn(testGuardian);

        // When
        Guardian result = guardianService.createGuardian(newGuardian);

        // Then
        assertThat(result).isNotNull();
        verify(userService).findUserEntityById(userId);
        verify(guardianRepository).findByUserId(userId);
        verify(guardianRepository).save(any(Guardian.class));
    }

    @Test
    void createGuardian_WhenUserNotFound_ShouldThrowResourceNotFoundException() {
        // Given
        Guardian newGuardian = new Guardian();
        newGuardian.setUserId(userId);
        newGuardian.setRelationship("Parent");

        when(userService.findUserEntityById(userId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> guardianService.createGuardian(newGuardian))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("User not found with ID: " + userId);
        verify(userService).findUserEntityById(userId);
        verifyNoMoreInteractions(guardianRepository);
    }

    @Test
    void createGuardian_WhenGuardianAlreadyExists_ShouldThrowValidationException() {
        // Given
        Guardian newGuardian = new Guardian();
        newGuardian.setUserId(userId);
        newGuardian.setRelationship("Parent");

        when(userService.findUserEntityById(userId)).thenReturn(Optional.of(testUser));
        when(guardianRepository.findByUserId(userId)).thenReturn(Optional.of(testGuardian));

        // When & Then
        assertThatThrownBy(() -> guardianService.createGuardian(newGuardian))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Guardian already exists for this user");
        verify(guardianRepository).findByUserId(userId);
        verify(guardianRepository, never()).save(any());
    }

    @Test
    void createGuardian_WithNullUserId_ShouldThrowValidationException() {
        // Given
        Guardian newGuardian = new Guardian();
        newGuardian.setUserId(null);
        newGuardian.setRelationship("Parent");

        // When & Then
        assertThatThrownBy(() -> guardianService.createGuardian(newGuardian))
                .isInstanceOf(ValidationException.class)
                .hasMessage("User ID is required");
        verifyNoInteractions(userService, guardianRepository);
    }

    @Test
    void createGuardian_WithEmptyRelationship_ShouldThrowValidationException() {
        // Given
        Guardian newGuardian = new Guardian();
        newGuardian.setUserId(userId);
        newGuardian.setRelationship("");

        // When & Then
        assertThatThrownBy(() -> guardianService.createGuardian(newGuardian))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Relationship is required");
        verifyNoInteractions(userService, guardianRepository);
    }

    @Test
    void createGuardian_WithNullRelationship_ShouldThrowValidationException() {
        // Given
        Guardian newGuardian = new Guardian();
        newGuardian.setUserId(userId);
        newGuardian.setRelationship(null);

        // When & Then
        assertThatThrownBy(() -> guardianService.createGuardian(newGuardian))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Relationship is required");
        verifyNoInteractions(userService, guardianRepository);
    }

    @Test
    void updateGuardian_WithValidData_ShouldUpdateGuardian() {
        // Given
        Guardian updateData = new Guardian();
        updateData.setUserId(userId); // Must include userId for validation
        updateData.setRelationship("Grandparent");
        updateData.setEmergencyContact("Secondary contact");
        updateData.setPickupAuthorized(true);

        when(guardianRepository.findById(guardianId)).thenReturn(Optional.of(testGuardian));
        when(guardianRepository.save(testGuardian)).thenReturn(testGuardian);

        // When
        Guardian result = guardianService.updateGuardian(guardianId, updateData);

        // Then
        assertThat(result).isNotNull();
        assertThat(testGuardian.getRelationship()).isEqualTo("Grandparent");
        assertThat(testGuardian.getEmergencyContact()).isEqualTo("Secondary contact");
        verify(guardianRepository).findById(guardianId);
        verify(guardianRepository).save(testGuardian);
    }

    @Test
    void updateGuardian_WhenGuardianNotFound_ShouldThrowResourceNotFoundException() {
        // Given
        Guardian updateData = new Guardian();
        updateData.setUserId(guardianId); // Dummy userId for validation
        updateData.setRelationship("Grandparent");

        when(guardianRepository.findById(guardianId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> guardianService.updateGuardian(guardianId, updateData))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Guardian not found with ID: " + guardianId);
        verify(guardianRepository).findById(guardianId);
        verify(guardianRepository, never()).save(any());
    }

    @Test
    void updateGuardian_WithNewUserId_ShouldUpdateUserReference() {
        // Given
        UUID newUserId = UUID.randomUUID();
        User newUser = new User();
        newUser.setId(newUserId);

        Guardian updateData = new Guardian();
        updateData.setUserId(newUserId);
        updateData.setRelationship("Stepparent");

        when(guardianRepository.findById(guardianId)).thenReturn(Optional.of(testGuardian));
        when(userService.findUserEntityById(newUserId)).thenReturn(Optional.of(newUser));
        when(guardianRepository.save(testGuardian)).thenReturn(testGuardian);

        // When
        Guardian result = guardianService.updateGuardian(guardianId, updateData);

        // Then
        assertThat(result).isNotNull();
        assertThat(testGuardian.getUserId()).isEqualTo(newUserId);
        verify(userService).findUserEntityById(newUserId);
        verify(guardianRepository).save(testGuardian);
    }

    @Test
    void updateGuardian_WithInvalidNewUserId_ShouldThrowResourceNotFoundException() {
        // Given
        UUID newUserId = UUID.randomUUID();
        Guardian updateData = new Guardian();
        updateData.setUserId(newUserId);
        updateData.setRelationship("Stepparent");

        when(guardianRepository.findById(guardianId)).thenReturn(Optional.of(testGuardian));
        when(userService.findUserEntityById(newUserId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> guardianService.updateGuardian(guardianId, updateData))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("User not found with ID: " + newUserId);
        verify(userService).findUserEntityById(newUserId);
        verify(guardianRepository, never()).save(any());
    }

    @Test
    void deleteGuardian_WhenGuardianExists_ShouldDeleteGuardian() {
        // Given
        when(guardianRepository.existsById(guardianId)).thenReturn(true);

        // When
        guardianService.deleteGuardian(guardianId);

        // Then
        verify(guardianRepository).existsById(guardianId);
        verify(guardianRepository).deleteById(guardianId);
    }

    @Test
    void deleteGuardian_WhenGuardianDoesNotExist_ShouldThrowResourceNotFoundException() {
        // Given
        when(guardianRepository.existsById(guardianId)).thenReturn(false);

        // When & Then
        assertThatThrownBy(() -> guardianService.deleteGuardian(guardianId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Guardian not found with ID: " + guardianId);
        verify(guardianRepository).existsById(guardianId);
        verify(guardianRepository, never()).deleteById(any());
    }

    @Test
    void getGuardianByUserId_WhenGuardianExists_ShouldReturnGuardian() {
        // Given
        when(guardianRepository.findByUserId(userId)).thenReturn(Optional.of(testGuardian));

        // When
        Guardian result = guardianService.getGuardianByUserId(userId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getUserId()).isEqualTo(userId);
        verify(guardianRepository).findByUserId(userId);
    }

    @Test
    void getGuardianByUserId_WhenGuardianNotFound_ShouldThrowResourceNotFoundException() {
        // Given
        when(guardianRepository.findByUserId(userId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> guardianService.getGuardianByUserId(userId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Guardian not found for user ID: " + userId);
        verify(guardianRepository).findByUserId(userId);
    }
}