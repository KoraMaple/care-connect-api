package com.careconnect.coreapi.childmgmt.api;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Command for updating child information.
 */
public record UpdateChildCommand(
    @NotNull(message = "Child ID is required")
    UUID childId,
    
    String firstName,
    String lastName,
    LocalDate dateOfBirth,
    String gender,
    String medicalInfo,
    String allergies
) {
    
    public static UpdateChildCommand of(UUID childId, String firstName, String lastName) {
        return new UpdateChildCommand(childId, firstName, lastName, null, null, null, null);
    }
}
