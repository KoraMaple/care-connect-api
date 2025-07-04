package com.careconnect.coreapi.childmgmt.events;

import java.time.Instant;
import java.util.UUID;

/**
 * Event published when a new child is registered in the system.
 */
public final class ChildRegisteredEvent extends ChildEvent {
    
    private final String firstName;
    private final String lastName;
    
    public ChildRegisteredEvent(UUID childId, String firstName, String lastName, Instant occurredAt) {
        super(childId, occurredAt);
        this.firstName = firstName;
        this.lastName = lastName;
    }
    
    public static ChildRegisteredEvent now(UUID childId, String firstName, String lastName) {
        return new ChildRegisteredEvent(childId, firstName, lastName, Instant.now());
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
}
