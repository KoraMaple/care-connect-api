package com.careconnect.coreapi.user.events;

import java.time.Instant;
import java.util.UUID;

/**
 * Base class for all user-related domain events.
 * Following Domain-Driven Design principles for event modeling.
 */
public abstract sealed class UserEvent permits UserRegisteredEvent, UserUpdatedEvent, UserDeletedEvent {
    
    private final UUID userId;
    private final String clerkUserId;
    private final Instant occurredAt;
    
    protected UserEvent(UUID userId, String clerkUserId, Instant occurredAt) {
        this.userId = userId;
        this.clerkUserId = clerkUserId;
        this.occurredAt = occurredAt;
    }
    
    public UUID getUserId() {
        return userId;
    }
    
    public String getClerkUserId() {
        return clerkUserId;
    }
    
    public Instant getOccurredAt() {
        return occurredAt;
    }
}
