package com.careconnect.coreapi.childmgmt.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

/**
 * Data Transfer Object for Child entity to be used in API responses.
 * This DTO exposes only the necessary fields and avoids Hibernate lazy loading issues.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChildDTO {
    
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
    
    // Guardian information (if needed) - using UUID instead of entity reference
    private UUID guardianId;
}
