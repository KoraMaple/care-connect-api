package com.careconnect.coreapi.childmgmt.internal;

import com.careconnect.coreapi.childmgmt.api.ChildDTO;
import com.careconnect.coreapi.childmgmt.domain.Child;
import org.springframework.stereotype.Component;

/**
 * Mapper to convert between Child entity and ChildDTO
 */
@Component
public class ChildMapper {

    /**
     * Convert Child entity to ChildDTO
     */
    public ChildDTO toDTO(Child child) {
        if (child == null) {
            return null;
        }

        return ChildDTO.builder()
                .id(child.getId())
                .firstName(child.getFirstName())
                .lastName(child.getLastName())
                .dob(child.getDob())
                .gender(child.getGender())
                .specialNeeds(child.getSpecialNeeds())
                .emergencyContact(child.getEmergencyContact())
                .createdAt(child.getCreatedAt())
                .updatedAt(child.getUpdatedAt())
                .guardianId(child.getGuardian() != null ? child.getGuardian().getId() : null)
                .build();
    }

    /**
     * Convert ChildDTO to Child entity (for updates)
     */
    public Child toEntity(ChildDTO childDTO) {
        if (childDTO == null) {
            return null;
        }

        Child child = new Child();
        child.setId(childDTO.getId());
        child.setFirstName(childDTO.getFirstName());
        child.setLastName(childDTO.getLastName());
        child.setDob(childDTO.getDob());
        child.setGender(childDTO.getGender());
        child.setSpecialNeeds(childDTO.getSpecialNeeds());
        child.setEmergencyContact(childDTO.getEmergencyContact());
        child.setCreatedAt(childDTO.getCreatedAt());
        child.setUpdatedAt(childDTO.getUpdatedAt());
        // Note: Guardian relationship is not set here to avoid lazy loading issues
        
        return child;
    }

    /**
     * Update existing entity from DTO
     */
    public void updateEntityFromDTO(Child entity, ChildDTO dto) {
        if (entity == null || dto == null) {
            return;
        }

        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setDob(dto.getDob());
        entity.setGender(dto.getGender());
        entity.setSpecialNeeds(dto.getSpecialNeeds());
        entity.setEmergencyContact(dto.getEmergencyContact());
        // Note: Guardian relationship and timestamps are managed separately
    }
}
