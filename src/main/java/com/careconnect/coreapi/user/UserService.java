package com.careconnect.coreapi.user;

import java.util.Optional;
import java.util.UUID;

/**
 * Public API service for accessing User information from other modules
 */
public interface UserService {
    
    /**
     * Find user information by ID
     */
    Optional<UserInfo> findUserById(UUID userId);
    
    /**
     * Find user information by Clerk user ID
     */
    Optional<UserInfo> findUserByClerkId(String clerkUserId);
    
    /**
     * Find user entity by ID (for internal module relationships)
     */
    Optional<User> findUserEntityById(UUID userId);
    
}
