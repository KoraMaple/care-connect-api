package com.careconnect.coreapi.childmgmt.service;

import com.careconnect.coreapi.childmgmt.jpa.Child;
import com.careconnect.coreapi.childmgmt.jpa.ChildGuardian;
import com.careconnect.coreapi.childmgmt.jpa.Guardian;
import com.careconnect.coreapi.childmgmt.repository.ChildGuardianRepository;
import com.careconnect.coreapi.childmgmt.repository.ChildRepository;
import com.careconnect.coreapi.childmgmt.repository.GuardianRepository;
import com.careconnect.coreapi.common.exceptions.DuplicateResourceException;
import com.careconnect.coreapi.common.exceptions.ResourceNotFoundException;
import com.careconnect.coreapi.common.exceptions.ValidationException;
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
public class ChildGuardianService {

    private final ChildGuardianRepository childGuardianRepository;
    private final ChildRepository childRepository;
    private final GuardianRepository guardianRepository;

    public ChildGuardian addGuardianToChild(UUID childId, UUID guardianId, Boolean isPrimary) {
        log.debug("Adding guardian {} to child {}, primary: {}", guardianId, childId, isPrimary);

        // Validate child exists
        Child child = childRepository.findById(childId)
                .orElseThrow(() -> new ResourceNotFoundException("Child not found with ID: " + childId));

        // Validate guardian exists
        Guardian guardian = guardianRepository.findById(guardianId)
                .orElseThrow(() -> new ResourceNotFoundException("Guardian not found with ID: " + guardianId));

        // Check if relationship already exists
        if (childGuardianRepository.existsByChildIdAndGuardianId(childId, guardianId)) {
            throw new DuplicateResourceException("Guardian is already associated with this child");
        }

        // If setting as primary, ensure no other primary guardian exists
        if (isPrimary != null && isPrimary) {
            childGuardianRepository.findPrimaryGuardianByChildId(childId)
                    .ifPresent(existing -> {
                        throw new ValidationException("Child already has a primary guardian");
                    });
        }

        ChildGuardian childGuardian = new ChildGuardian();
        childGuardian.setChild(child);
        childGuardian.setGuardian(guardian);
        childGuardian.setPrimaryGuardian(isPrimary != null ? isPrimary : false);
        childGuardian.setCreatedAt(Instant.now());
        childGuardian.setUpdatedAt(Instant.now());

        ChildGuardian saved = childGuardianRepository.save(childGuardian);
        log.info("Added guardian {} to child {}", guardianId, childId);

        return saved;
    }

    public List<Guardian> getGuardiansForChild(UUID childId) {
        log.debug("Fetching guardians for child ID: {}", childId);

        if (!childRepository.existsById(childId)) {
            throw new ResourceNotFoundException("Child not found with ID: " + childId);
        }

        List<ChildGuardian> childGuardians = childGuardianRepository.findByChildId(childId);
        return childGuardians.stream()
                .map(ChildGuardian::getGuardian)
                .toList();
    }

    public List<ChildGuardian> getChildGuardianRelationships(UUID childId) {
        log.debug("Fetching child-guardian relationships for child ID: {}", childId);

        if (!childRepository.existsById(childId)) {
            throw new ResourceNotFoundException("Child not found with ID: " + childId);
        }

        return childGuardianRepository.findByChildId(childId);
    }

    public void removeGuardianFromChild(UUID childId, UUID guardianId) {
        log.debug("Removing guardian {} from child {}", guardianId, childId);

        ChildGuardian relationship = childGuardianRepository.findByChildIdAndGuardianId(childId, guardianId)
                .orElseThrow(() -> new ResourceNotFoundException("Guardian relationship not found"));

        // Check if this is the only guardian
        Long guardianCount = childGuardianRepository.countByChildId(childId);
        if (guardianCount <= 1) {
            throw new ValidationException("Cannot remove the last guardian from a child");
        }

        childGuardianRepository.delete(relationship);
        log.info("Removed guardian {} from child {}", guardianId, childId);
    }

    public ChildGuardian updatePrimaryGuardian(UUID childId, UUID guardianId) {
        log.debug("Setting guardian {} as primary for child {}", guardianId, childId);

        // Find the relationship
        ChildGuardian relationship = childGuardianRepository.findByChildIdAndGuardianId(childId, guardianId)
                .orElseThrow(() -> new ResourceNotFoundException("Guardian relationship not found"));

        // Remove primary status from any existing primary guardian
        childGuardianRepository.findPrimaryGuardianByChildId(childId)
                .ifPresent(existing -> {
                    existing.setPrimaryGuardian(false);
                    existing.setUpdatedAt(Instant.now());
                    childGuardianRepository.save(existing);
                });

        // Set new primary guardian
        relationship.setPrimaryGuardian(true);
        relationship.setUpdatedAt(Instant.now());

        ChildGuardian updated = childGuardianRepository.save(relationship);
        log.info("Set guardian {} as primary for child {}", guardianId, childId);

        return updated;
    }
}
