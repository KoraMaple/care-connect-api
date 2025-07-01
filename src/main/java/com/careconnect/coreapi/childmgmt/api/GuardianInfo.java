package com.careconnect.coreapi.childmgmt.api;

import java.util.UUID;

/**
 * Public API for Guardian information exposed to other modules
 */
public record GuardianInfo(
    UUID id,
    String relationship,
    String emergencyContact,
    Boolean pickupAuthorized,
    UUID userId
) {
    
}
