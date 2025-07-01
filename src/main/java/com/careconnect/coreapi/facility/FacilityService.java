package com.careconnect.coreapi.facility;

import java.util.Optional;
import java.util.UUID;

/**
 * Public API service for accessing Facility information from other modules
 */
public interface FacilityService {
    
    /**
     * Find facility information by ID
     */
    Optional<FacilityInfo> findFacilityById(UUID facilityId);
    
}
