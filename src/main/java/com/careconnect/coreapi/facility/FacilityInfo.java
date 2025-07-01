package com.careconnect.coreapi.facility;

import java.util.UUID;

/**
 * Public API for Facility information exposed to other modules
 */
public record FacilityInfo(
    UUID id,
    String name,
    String phone,
    String licenseNumber,
    Integer maxCapacity
) {
    
}
