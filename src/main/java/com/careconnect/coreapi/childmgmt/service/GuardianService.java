package com.careconnect.coreapi.childmgmt.service;

import com.careconnect.coreapi.childmgmt.jpa.Guardian;
import com.careconnect.coreapi.childmgmt.repository.GuardianRepository;
import com.careconnect.coreapi.common.exceptions.ResourceNotFoundException;
import com.careconnect.coreapi.common.exceptions.ValidationException;
import com.careconnect.coreapi.user.User;
import com.careconnect.coreapi.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class GuardianService {

    private final GuardianRepository guardianRepository;
    private final UserService userService; // Use service interface instead of repository

    public List<Guardian> getAllGuardians() {
        log.debug("Fetching all guardians");
        return guardianRepository.findAll();
    }

    public Guardian getGuardianById(UUID id) {
        log.debug("Fetching guardian by ID: {}", id);
        return guardianRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Guardian not found with ID: " + id));
    }

    public Guardian createGuardian(Guardian guardian) {
        log.debug("Creating new guardian for user ID: {}", guardian.getUser().getId());

        validateGuardianData(guardian);

        // Check if user exists
        User user = userService.findUserEntityById(guardian.getUser().getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + guardian.getUser().getId()));

        // Check if guardian already exists for this user
        if (guardianRepository.findByUserId(guardian.getUser().getId()).isPresent()) {
            throw new ValidationException("Guardian already exists for this user");
        }

        guardian.setId(null); // Ensure new entity
        guardian.setUser(user);
        guardian.setCreatedAt(Instant.now());
        guardian.setUpdatedAt(Instant.now());

        Guardian savedGuardian = guardianRepository.save(guardian);
        log.info("Created guardian with ID: {}", savedGuardian.getId());

        return savedGuardian;
    }

    public Guardian updateGuardian(UUID id, Guardian guardianData) {
        log.debug("Updating guardian with ID: {}", id);

        Guardian existingGuardian = guardianRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Guardian not found with ID: " + id));

        validateGuardianData(guardianData);

        // Update fields
        existingGuardian.setRelationship(guardianData.getRelationship());
        existingGuardian.setEmergencyContact(guardianData.getEmergencyContact());
        existingGuardian.setPickupAuthorized(guardianData.getPickupAuthorized());
        existingGuardian.setUpdatedAt(Instant.now());

        // Update user if provided and different
        if (guardianData.getUser() != null && guardianData.getUser().getId() != null &&
            !guardianData.getUser().getId().equals(existingGuardian.getUser().getId())) {
            User user = userService.findUserEntityById(guardianData.getUser().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + guardianData.getUser().getId()));
            existingGuardian.setUser(user);
        }

        Guardian updatedGuardian = guardianRepository.save(existingGuardian);
        log.info("Updated guardian with ID: {}", updatedGuardian.getId());

        return updatedGuardian;
    }

    public void deleteGuardian(UUID id) {
        log.debug("Deleting guardian with ID: {}", id);

        if (!guardianRepository.existsById(id)) {
            throw new ResourceNotFoundException("Guardian not found with ID: " + id);
        }

        guardianRepository.deleteById(id);
        log.info("Deleted guardian with ID: {}", id);
    }

    public Guardian getGuardianByUserId(UUID userId) {
        log.debug("Fetching guardian by user ID: {}", userId);
        return guardianRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Guardian not found for user ID: " + userId));
    }

    private void validateGuardianData(Guardian guardian) {
        if (guardian.getUser() == null || guardian.getUser().getId() == null) {
            throw new ValidationException("User is required");
        }

        if (guardian.getRelationship() == null || guardian.getRelationship().trim().isEmpty()) {
            throw new ValidationException("Relationship is required");
        }
    }
}
