package com.careconnect.coreapi.user.events;

import java.time.Instant;
import java.util.UUID;

/**
 * Event published when a user is deleted from the system.
 */
public final class UserDeletedEvent extends UserEvent {
    
    public UserDeletedEvent(UUID userId, String clerkUserId, Instant occurredAt) {
        super(userId, clerkUserId, occurredAt);
    }
    
    public static UserDeletedEvent now(UUID userId, String clerkUserId) {
        return new UserDeletedEvent(userId, clerkUserId, Instant.now());
    }
}
