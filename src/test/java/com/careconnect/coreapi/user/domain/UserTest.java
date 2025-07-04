package com.careconnect.coreapi.user.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
    }

    @Test
    void setAndGetId_ShouldWorkCorrectly() {
        // Given
        UUID id = UUID.randomUUID();

        // When
        user.setId(id);

        // Then
        assertThat(user.getId()).isEqualTo(id);
    }

    @Test
    void setAndGetClerkUserId_ShouldWorkCorrectly() {
        // Given
        String clerkUserId = "clerk_123";

        // When
        user.setClerkUserId(clerkUserId);

        // Then
        assertThat(user.getClerkUserId()).isEqualTo(clerkUserId);
    }

    @Test
    void setAndGetCreatedAt_ShouldWorkCorrectly() {
        // Given
        Instant createdAt = Instant.now();

        // When
        user.setCreatedAt(createdAt);

        // Then
        assertThat(user.getCreatedAt()).isEqualTo(createdAt);
    }

    @Test
    void setAndGetUpdatedAt_ShouldWorkCorrectly() {
        // Given
        Instant updatedAt = Instant.now();

        // When
        user.setUpdatedAt(updatedAt);

        // Then
        assertThat(user.getUpdatedAt()).isEqualTo(updatedAt);
    }

    @Test
    void newUser_ShouldHaveNullValues() {
        // Given
        User newUser = new User();

        // Then
        assertThat(newUser.getId()).isNull();
        assertThat(newUser.getClerkUserId()).isNull();
        assertThat(newUser.getCreatedAt()).isNull();
        assertThat(newUser.getUpdatedAt()).isNull();
    }
}
