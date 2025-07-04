package com.careconnect.coreapi.user;

import com.careconnect.coreapi.user.domain.User;

import java.time.Instant;
import java.util.UUID;

/**
 * Data Transfer Object for User information exposed to other modules.
 * 
 * Design Principles:
 * - Immutable record for thread safety
 * - Contains only data needed by other modules
 * - Factory methods for easy conversion from domain entities
 */
public record UserInfo(
    UUID id,
    String clerkUserId,
    Instant createdAt,
    Instant updatedAt
) {
    
    /**
     * Factory method to create UserInfo from User domain entity.
     */
    public static UserInfo fromUser(User user) {
        return new UserInfo(
            user.getId(),
            user.getClerkUserId(),
            user.getCreatedAt(),
            user.getUpdatedAt()
        );
    }
}
