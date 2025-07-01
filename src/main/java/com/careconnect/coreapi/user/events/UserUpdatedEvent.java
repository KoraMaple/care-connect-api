package com.careconnect.coreapi.user.events;

import java.time.Instant;
import java.util.UUID;

/**
 * Event published when a user's information is updated.
 */
public final class UserUpdatedEvent extends UserEvent {
    
    public UserUpdatedEvent(UUID userId, String clerkUserId, Instant occurredAt) {
        super(userId, clerkUserId, occurredAt);
    }
    
    public static UserUpdatedEvent now(UUID userId, String clerkUserId) {
        return new UserUpdatedEvent(userId, clerkUserId, Instant.now());
    }
}
