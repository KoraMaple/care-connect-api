package com.careconnect.coreapi.childmgmt.dto;

import com.careconnect.coreapi.childmgmt.domain.Child;
import com.careconnect.coreapi.common.utils.XSSProtectionUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

/**
 * DTO for Child creation/update requests that sanitizes user input to prevent XSS attacks.
 * This class ensures all string fields are properly sanitized before processing.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChildRequestDto {
    
    @NotBlank(message = "First name is required")
    @Size(max = 100, message = "First name must not exceed 100 characters")
    private String firstName;
    
    @NotBlank(message = "Last name is required")
    @Size(max = 100, message = "Last name must not exceed 100 characters")
    private String lastName;
    
    @NotNull(message = "Date of birth is required")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant dob;
    
    @Size(max = 10, message = "Gender must not exceed 10 characters")
    private String gender;
    
    @Size(max = 1000, message = "Special needs must not exceed 1000 characters")
    private String specialNeeds;
    
    @Size(max = 500, message = "Emergency contact must not exceed 500 characters")
    private String emergencyContact;
    
    // Guardian ID (optional for creation, used for legacy relationship)
    private UUID guardianId;
    
    /**
     * Converts a sanitized ChildRequestDto to a Child entity.
     * All user-input fields are sanitized to prevent XSS attacks.
     * 
     * @return Child entity with sanitized data
     */
    public Child toEntity() {
        Child child = new Child();
        child.setFirstName(XSSProtectionUtil.sanitizeText(this.firstName));
        child.setLastName(XSSProtectionUtil.sanitizeText(this.lastName));
        child.setDob(this.dob);
        child.setGender(XSSProtectionUtil.sanitizeText(this.gender));
        child.setSpecialNeeds(XSSProtectionUtil.sanitizeFormattedText(this.specialNeeds));
        child.setEmergencyContact(XSSProtectionUtil.sanitizeText(this.emergencyContact));
        
        // Set timestamps
        Instant now = Instant.now();
        child.setCreatedAt(now);
        child.setUpdatedAt(now);
        
        return child;
    }
    
    /**
     * Updates an existing Child entity with sanitized data from this DTO.
     * 
     * @param existingChild the existing Child entity to update
     * @return updated Child entity
     */
    public Child updateEntity(Child existingChild) {
        existingChild.setFirstName(XSSProtectionUtil.sanitizeText(this.firstName));
        existingChild.setLastName(XSSProtectionUtil.sanitizeText(this.lastName));
        existingChild.setDob(this.dob);
        existingChild.setGender(XSSProtectionUtil.sanitizeText(this.gender));
        existingChild.setSpecialNeeds(XSSProtectionUtil.sanitizeFormattedText(this.specialNeeds));
        existingChild.setEmergencyContact(XSSProtectionUtil.sanitizeText(this.emergencyContact));
        existingChild.setUpdatedAt(Instant.now());
        
        return existingChild;
    }
}
