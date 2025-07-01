package com.careconnect.coreapi.childmgmt.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

/**
 * Command for registering a new child in the system.
 */
public record RegisterChildCommand(
    @NotBlank(message = "First name is required")
    String firstName,
    
    @NotBlank(message = "Last name is required") 
    String lastName,
    
    @NotNull(message = "Date of birth is required")
    LocalDate dateOfBirth,
    
    String gender,
    String medicalInfo,
    String allergies
) {
    
    public static RegisterChildCommand of(String firstName, String lastName, LocalDate dateOfBirth) {
        return new RegisterChildCommand(firstName, lastName, dateOfBirth, null, null, null);
    }
}
