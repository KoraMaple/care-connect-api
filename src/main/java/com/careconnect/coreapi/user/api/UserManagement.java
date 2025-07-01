package com.careconnect.coreapi.user.api;

import com.careconnect.coreapi.user.UserInfo;

import java.util.Optional;
import java.util.UUID;

/**
 * Public API for User module operations.
 * This interface is the only way other modules should interact with User functionality.
 * 
 * Design Principles:
 * - Return DTOs (UserInfo) instead of entities for loose coupling
 * - Use Optional for null safety
 * - Clear, intention-revealing method names
 */
public interface UserManagement {
    
    /**
     * Find user information by user ID.
     * @param userId the unique user identifier
     * @return UserInfo if found, empty Optional otherwise
     */
    Optional<UserInfo> findUserById(UUID userId);
    
    /**
     * Find user information by Clerk authentication ID.
     * @param clerkUserId the Clerk user identifier
     * @return UserInfo if found, empty Optional otherwise
     */
    Optional<UserInfo> findUserByClerkId(String clerkUserId);
    
    /**
     * Register a new user in the system.
     * @param command the registration command containing user details
     * @return the created user information
     */
    UserInfo registerUser(RegisterUserCommand command);
    
    /**
     * Update existing user information.
     * @param command the update command containing changed user details
     * @return the updated user information
     */
    UserInfo updateUser(UpdateUserCommand command);
    
    /**
     * Delete a user from the system.
     * @param userId the unique user identifier
     */
    void deleteUser(UUID userId);
}
