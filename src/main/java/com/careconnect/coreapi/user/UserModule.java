package com.careconnect.coreapi.user;

import org.springframework.context.annotation.Configuration;
import org.springframework.modulith.ApplicationModule;

/**
 * User module using CONVENTION OVER CONFIGURATION approach.
 * 
 * EXPOSED BY DEFAULT (no package-info.java needed):
 * - User.java (entity)
 * - UserService.java (interface) 
 * - UserInfo.java (DTO)
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
public class UserModule {
    // Convention over Configuration:
    // - Public API classes are in the root package (auto-exposed)
    // - Implementation details are in internal/ package (auto-hidden)
}
