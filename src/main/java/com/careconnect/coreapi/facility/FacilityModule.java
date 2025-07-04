package com.careconnect.coreapi.facility;

import org.springframework.context.annotation.Configuration;
import org.springframework.modulith.ApplicationModule;

/**
 * Facility module using CONVENTION OVER CONFIGURATION approach.
 * 
 * EXPOSED BY DEFAULT (no package-info.java needed):
 * - Facility.java (entity)
 * - Address.java (entity)
 * - FacilityService.java (interface)
 * - FacilityInfo.java (DTO)
 * 
 * HIDDEN BY DEFAULT:
 * - internal.* (all implementation details)
 * 
 * RESULT: Zero package-info.java files needed!
 */
@Configuration
@ApplicationModule(
    allowedDependencies = {"common"}
)
public class FacilityModule {
    // Convention over Configuration:
    // - Public API classes are in the root package (auto-exposed)
    // - Implementation details are in internal/ package (auto-hidden)
}
