package com.careconnect.coreapi.user.events;

import java.time.Instant;
import java.util.UUID;

/**
 * Event published when a new user is registered in the system.
 * Other modules can listen to this event to trigger downstream processes.
 */
public final class UserRegisteredEvent extends UserEvent {
    
    public UserRegisteredEvent(UUID userId, String clerkUserId, Instant occurredAt) {
        super(userId, clerkUserId, occurredAt);
    }
    
    public static UserRegisteredEvent now(UUID userId, String clerkUserId) {
        return new UserRegisteredEvent(userId, clerkUserId, Instant.now());
    }
}
