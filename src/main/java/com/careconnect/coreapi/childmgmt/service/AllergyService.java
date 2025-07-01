package com.careconnect.coreapi.childmgmt.service;

import com.careconnect.coreapi.childmgmt.jpa.Allergy;
import com.careconnect.coreapi.childmgmt.jpa.Child;
import com.careconnect.coreapi.childmgmt.jpa.ChildAllergy;
import com.careconnect.coreapi.childmgmt.repository.AllergyRepository;
import com.careconnect.coreapi.childmgmt.repository.ChildAllergyRepository;
import com.careconnect.coreapi.childmgmt.repository.ChildRepository;
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
public class AllergyService {

    private final AllergyRepository allergyRepository;
    private final ChildAllergyRepository childAllergyRepository;
    private final ChildRepository childRepository;

    public List<Allergy> getAllAllergies() {
        log.debug("Fetching all allergies");
        return allergyRepository.findAll();
    }

    public Allergy getAllergyById(UUID id) {
        log.debug("Fetching allergy by ID: {}", id);
        return allergyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Allergy not found with ID: " + id));
    }

    public Allergy createAllergy(Allergy allergy) {
        log.debug("Creating new allergy: {}", allergy.getName());

        validateAllergyData(allergy);

        // Check if allergy name already exists
        if (allergyRepository.findByNameIgnoreCase(allergy.getName()).isPresent()) {
            throw new DuplicateResourceException("Allergy with name '" + allergy.getName() + "' already exists");
        }

        allergy.setId(null); // Ensure new entity
        allergy.setCreatedAt(Instant.now());
        allergy.setUpdatedAt(Instant.now());

        Allergy savedAllergy = allergyRepository.save(allergy);
        log.info("Created allergy with ID: {}", savedAllergy.getId());

        return savedAllergy;
    }

    public ChildAllergy addAllergyToChild(UUID childId, UUID allergyId, String notes) {
        log.debug("Adding allergy {} to child {}", allergyId, childId);

        // Validate child exists
        Child child = childRepository.findById(childId)
                .orElseThrow(() -> new ResourceNotFoundException("Child not found with ID: " + childId));

        // Validate allergy exists
        Allergy allergy = allergyRepository.findById(allergyId)
                .orElseThrow(() -> new ResourceNotFoundException("Allergy not found with ID: " + allergyId));

        // Check if relationship already exists
        if (childAllergyRepository.existsByChildIdAndAllergyId(childId, allergyId)) {
            throw new DuplicateResourceException("Allergy is already assigned to this child");
        }

        ChildAllergy childAllergy = new ChildAllergy();
        childAllergy.setChild(child);
        childAllergy.setAllergy(allergy);
        childAllergy.setNotes(notes);
        childAllergy.setCreatedAt(Instant.now());
        childAllergy.setUpdatedAt(Instant.now());

        ChildAllergy saved = childAllergyRepository.save(childAllergy);
        log.info("Added allergy {} to child {}", allergyId, childId);

        return saved;
    }

    public List<ChildAllergy> getAllergiesForChild(UUID childId) {
        log.debug("Fetching allergies for child ID: {}", childId);

        if (!childRepository.existsById(childId)) {
            throw new ResourceNotFoundException("Child not found with ID: " + childId);
        }

        return childAllergyRepository.findByChildId(childId);
    }

    public void removeAllergyFromChild(UUID childId, UUID allergyId) {
        log.debug("Removing allergy {} from child {}", allergyId, childId);

        if (!childAllergyRepository.existsByChildIdAndAllergyId(childId, allergyId)) {
            throw new ResourceNotFoundException("Allergy relationship not found");
        }

        childAllergyRepository.deleteByChildIdAndAllergyId(childId, allergyId);
        log.info("Removed allergy {} from child {}", allergyId, childId);
    }

    public List<Allergy> searchAllergiesByName(String name) {
        log.debug("Searching allergies by name: {}", name);
        return allergyRepository.findByNameContainingIgnoreCase(name);
    }

    private void validateAllergyData(Allergy allergy) {
        if (allergy.getName() == null || allergy.getName().trim().isEmpty()) {
            throw new ValidationException("Allergy name is required");
        }
    }
}
