package com.careconnect.coreapi.childmgmt.api;

import java.util.UUID;

/**
 * Public API for Child information exposed to other modules
 */
public record ChildInfo(
    UUID id,
    String firstName,
    String lastName,
    String dateOfBirth
) {
    
}
