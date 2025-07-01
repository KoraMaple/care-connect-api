package com.careconnect.coreapi.user.api;

import jakarta.validation.constraints.NotBlank;

/**
 * Command for registering a new user in the system.
 * Follows Command Pattern for clear intent and validation.
 */
public record RegisterUserCommand(
    @NotBlank(message = "Clerk User ID is required")
    String clerkUserId
) {
    
    /**
     * Factory method for creating registration commands.
     */
    public static RegisterUserCommand of(String clerkUserId) {
        return new RegisterUserCommand(clerkUserId);
    }
}
