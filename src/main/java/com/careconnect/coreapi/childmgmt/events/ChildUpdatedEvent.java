package com.careconnect.coreapi.childmgmt.events;

import java.time.Instant;
import java.util.UUID;

/**
 * Event published when a child's information is updated.
 */
public final class ChildUpdatedEvent extends ChildEvent {
    
    public ChildUpdatedEvent(UUID childId, Instant occurredAt) {
        super(childId, occurredAt);
    }
    
    public static ChildUpdatedEvent now(UUID childId) {
        return new ChildUpdatedEvent(childId, Instant.now());
    }
}
