package com.careconnect.coreapi.childmgmt.api;

import java.util.Optional;
import java.util.UUID;

/**
 * Public API service for accessing Child and Guardian information from other modules
 */
public interface ChildManagementService {
    
    /**
     * Find child information by ID
     */
    Optional<ChildInfo> findChildById(UUID childId);
    
    /**
     * Find guardian information by ID
     */
    Optional<GuardianInfo> findGuardianById(UUID guardianId);
    
    /**
     * Find guardian information by User ID
     */
    Optional<GuardianInfo> findGuardianByUserId(UUID userId);
    
}
