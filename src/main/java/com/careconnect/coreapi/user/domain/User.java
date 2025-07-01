package com.careconnect.coreapi.user.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.util.UUID;

/**
 * User aggregate root representing a user in the CareConnect system.
 * 
 * Domain Model Principles:
 * - Rich domain model with behavior
 * - Encapsulation of business rules
 * - Clear lifecycle management
 */
@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    @Id
    @Column(name = "id", nullable = false)
    @ColumnDefault("gen_random_uuid()")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "clerk_user_id", nullable = false, length = 255)
    private String clerkUserId;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
    
    /**
     * Factory method for creating new users.
     */
    public static User createNewUser(String clerkUserId) {
        User user = new User();
        user.setClerkUserId(clerkUserId);
        user.setCreatedAt(Instant.now());
        user.setUpdatedAt(Instant.now());
        return user;
    }
    
    /**
     * Updates the user's information.
     */
    public void updateClerkUserId(String newClerkUserId) {
        this.clerkUserId = newClerkUserId;
        this.updatedAt = Instant.now();
    }
    
    /**
     * Checks if this user was created by a specific Clerk user ID.
     */
    public boolean belongsToClerkUser(String clerkUserId) {
        return this.clerkUserId.equals(clerkUserId);
    }
}
