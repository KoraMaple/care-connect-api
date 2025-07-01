package com.careconnect.coreapi.user.api;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/**
 * Command for updating existing user information.
 * Follows Command Pattern for clear intent and validation.
 */
public record UpdateUserCommand(
    @NotNull(message = "User ID is required")
    UUID userId,
    
    String clerkUserId
) {
    
    /**
     * Factory method for creating update commands.
     */
    public static UpdateUserCommand of(UUID userId, String clerkUserId) {
        return new UpdateUserCommand(userId, clerkUserId);
    }
}
