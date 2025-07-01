package com.careconnect.coreapi.childmgmt.events;

import java.time.Instant;
import java.util.UUID;

/**
 * Base class for all child management domain events.
 */
public abstract sealed class ChildEvent permits ChildRegisteredEvent, ChildUpdatedEvent, GuardianAssignedEvent {
    
    private final UUID childId;
    private final Instant occurredAt;
    
    protected ChildEvent(UUID childId, Instant occurredAt) {
        this.childId = childId;
        this.occurredAt = occurredAt;
    }
    
    public UUID getChildId() {
        return childId;
    }
    
    public Instant getOccurredAt() {
        return occurredAt;
    }
}
