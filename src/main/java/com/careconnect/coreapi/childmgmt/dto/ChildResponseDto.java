package com.careconnect.coreapi.childmgmt.dto;

import com.careconnect.coreapi.childmgmt.domain.Child;
import com.careconnect.coreapi.common.utils.XSSProtectionUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * DTO for Child responses that sanitizes user input to prevent XSS attacks.
 * This class ensures all string fields are properly sanitized before being sent to clients.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChildResponseDto {
    
    private UUID id;
    private String firstName;
    private String lastName;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant dob;
    
    private String gender;
    private String specialNeeds;
    private String emergencyContact;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant createdAt;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant updatedAt;
    
    // Guardian information (safely exposed)
    private UUID primaryGuardianId;
    private List<UUID> allGuardianIds;
    
    /**
     * Creates a sanitized ChildResponseDto from a Child entity.
     * All user-input fields are sanitized to prevent XSS attacks.
     * 
     * @param child the Child entity to convert
     * @return sanitized ChildResponseDto
     */
    public static ChildResponseDto fromEntity(Child child) {
        if (child == null) {
            return null;
        }
        
        return ChildResponseDto.builder()
                .id(child.getId())
                .firstName(XSSProtectionUtil.sanitizeText(child.getFirstName()))
                .lastName(XSSProtectionUtil.sanitizeText(child.getLastName()))
                .dob(child.getDob())
                .gender(XSSProtectionUtil.sanitizeText(child.getGender()))
                .specialNeeds(XSSProtectionUtil.sanitizeFormattedText(child.getSpecialNeeds()))
                .emergencyContact(XSSProtectionUtil.sanitizeText(child.getEmergencyContact()))
                .createdAt(child.getCreatedAt())
                .updatedAt(child.getUpdatedAt())
                .primaryGuardianId(child.getPrimaryGuardianId())
                .allGuardianIds(child.getAllGuardianIds())
                .build();
    }
    
    /**
     * Converts a list of Child entities to sanitized DTOs.
     * 
     * @param children list of Child entities
     * @return list of sanitized ChildResponseDto objects
     */
    public static List<ChildResponseDto> fromEntityList(List<Child> children) {
        if (children == null) {
            return List.of();
        }
        
        return children.stream()
                .map(ChildResponseDto::fromEntity)
                .toList();
    }
}
