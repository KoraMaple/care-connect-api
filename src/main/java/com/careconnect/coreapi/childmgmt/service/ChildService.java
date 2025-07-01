package com.careconnect.coreapi.childmgmt.service;

import com.careconnect.coreapi.childmgmt.jpa.Child;
import com.careconnect.coreapi.childmgmt.repository.ChildRepository;
import com.careconnect.coreapi.childmgmt.repository.GuardianRepository;
import com.careconnect.coreapi.common.exceptions.ResourceNotFoundException;
import com.careconnect.coreapi.common.exceptions.ValidationException;
import com.careconnect.coreapi.common.response.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ChildService {

    private final ChildRepository childRepository;
    private final GuardianRepository guardianRepository;

    public PageResponse<Child> getAllChildren(int pageNumber, int pageSize) {
        log.debug("Fetching all children with pagination: {}", pageNumber);
//        return childRepository.findAll(pageable);
        Page<Child> childrenPage = childRepository.findAll(PageRequest.of(pageNumber, pageSize));

        return  new PageResponse<Child>(childrenPage);
    }

    public Child getChildById(UUID id) {
        log.debug("Fetching child by ID: {}", id);
        return childRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Child not found with ID: " + id));
    }

    public Child createChild(Child child) {
        log.debug("Creating new child: {} {}", child.getFirstName(), child.getLastName());

        validateChildData(child);

        child.setId(null); // Ensure new entity
        child.setCreatedAt(Instant.now());
        child.setUpdatedAt(Instant.now());

        Child savedChild = childRepository.save(child);
        log.info("Created child with ID: {}", savedChild.getId());

        return savedChild;
    }

    public Child updateChild(UUID id, Child childData) {
        log.debug("Updating child with ID: {}", id);

        Child existingChild = childRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Child not found with ID: " + id));

        validateChildData(childData);

        // Update fields
        existingChild.setFirstName(childData.getFirstName());
        existingChild.setLastName(childData.getLastName());
        existingChild.setDob(childData.getDob());
        existingChild.setGender(childData.getGender());
        existingChild.setSpecialNeeds(childData.getSpecialNeeds());
        existingChild.setEmergencyContact(childData.getEmergencyContact());
        existingChild.setUpdatedAt(Instant.now());

        if (childData.getGuardian() != null) {
            existingChild.setGuardian(childData.getGuardian());
        }

        Child updatedChild = childRepository.save(existingChild);
        log.info("Updated child with ID: {}", updatedChild.getId());

        return updatedChild;
    }

    public void deleteChild(UUID id) {
        log.debug("Deleting child with ID: {}", id);

        if (!childRepository.existsById(id)) {
            throw new ResourceNotFoundException("Child not found with ID: " + id);
        }

        childRepository.deleteById(id);
        log.info("Deleted child with ID: {}", id);
    }

    public Page<Child> searchChildren(String name, String gender, Pageable pageable) {
        log.debug("Searching children with name: {}, gender: {}", name, gender);
        return childRepository.findByFilters(name, gender, pageable);
    }

    public List<Child> getChildrenByGuardian(UUID guardianId) {
        log.debug("Fetching children for guardian ID: {}", guardianId);

        if (!guardianRepository.existsById(guardianId)) {
            throw new ResourceNotFoundException("Guardian not found with ID: " + guardianId);
        }

        return childRepository.findByAssociatedGuardianId(guardianId);
    }

    private void validateChildData(Child child) {
        if (child.getFirstName() == null || child.getFirstName().trim().isEmpty()) {
            throw new ValidationException("First name is required");
        }

        if (child.getLastName() == null || child.getLastName().trim().isEmpty()) {
            throw new ValidationException("Last name is required");
        }

        if (child.getDob() == null) {
            throw new ValidationException("Date of birth is required");
        }

        if (child.getDob().isAfter(Instant.now())) {
            throw new ValidationException("Date of birth cannot be in the future");
        }
    }
}
