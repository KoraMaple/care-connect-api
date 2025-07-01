package com.careconnect.coreapi.user;

import com.careconnect.coreapi.user.api.UserManagement;
import com.careconnect.coreapi.user.internal.UserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for User module components.
 * 
 * Design Principles:
 * - Explicit bean configuration for important APIs
 * - Clear separation of internal implementation from public API
 * - Module-specific configuration isolation
 */
@Configuration
public class UserModuleConfiguration {
    
    /**
     * Expose UserManagement API for other modules to use.
     * This allows other modules to depend on the interface rather than the implementation.
     */
    @Bean
    public UserManagement userManagement(UserServiceImpl userServiceImpl) {
        return userServiceImpl;
    }
}
