package com.careconnect.coreapi.user;

import org.springframework.context.annotation.Configuration;
import org.springframework.modulith.ApplicationModule;

/**
 * User Module - Domain Boundary Definition
 * 
 * This module is responsible for:
 * - User registration and management
 * - Integration with Clerk authentication system
 * - User lifecycle events
 * 
 * PUBLIC API (automatically exposed):
 * - UserService - Legacy interface for backwards compatibility
 * - UserInfo - Data transfer object
 * - api.UserManagement - Modern command/query API
 * - events.* - Domain events for other modules to listen to
 * 
 * INTERNAL IMPLEMENTATION (automatically hidden):
 * - internal.* - All implementation details
 * - domain.* - Domain entities and business logic
 * 
 * MODULE BOUNDARIES:
 * - Depends only on: common (shared utilities)
 * - No direct access to other domain modules
 * - Communicates via events for loose coupling
 */
@Configuration
@ApplicationModule(
    allowedDependencies = {"common"}
)
public class UserModule {
    // This class defines the module boundary using Spring Modulith conventions
    // No explicit @NamedInterface annotations needed - using package structure conventions
}
