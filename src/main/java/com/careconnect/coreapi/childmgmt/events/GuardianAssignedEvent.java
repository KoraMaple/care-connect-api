package com.careconnect.coreapi.childmgmt.events;

import java.time.Instant;
import java.util.UUID;

/**
 * Event published when a guardian is assigned to a child.
 */
public final class GuardianAssignedEvent extends ChildEvent {
    
    private final UUID guardianId;
    private final UUID userId;
    
    public GuardianAssignedEvent(UUID childId, UUID guardianId, UUID userId, Instant occurredAt) {
        super(childId, occurredAt);
        this.guardianId = guardianId;
        this.userId = userId;
    }
    
    public static GuardianAssignedEvent now(UUID childId, UUID guardianId, UUID userId) {
        return new GuardianAssignedEvent(childId, guardianId, userId, Instant.now());
    }
    
    public UUID getGuardianId() {
        return guardianId;
    }
    
    public UUID getUserId() {
        return userId;
    }
}
