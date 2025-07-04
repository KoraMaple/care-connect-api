package com.careconnect.coreapi.childmgmt.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class GuardianTest {

    private Guardian guardian;

    @BeforeEach
    void setUp() {
        guardian = new Guardian();
    }

    @Test
    void setAndGetId_ShouldWorkCorrectly() {
        // Given
        UUID id = UUID.randomUUID();

        // When
        guardian.setId(id);

        // Then
        assertThat(guardian.getId()).isEqualTo(id);
    }

    @Test
    void setAndGetUserId_ShouldWorkCorrectly() {
        // Given
        UUID userId = UUID.randomUUID();

        // When
        guardian.setUserId(userId);

        // Then
        assertThat(guardian.getUserId()).isEqualTo(userId);
    }

    @Test
    void setAndGetRelationship_ShouldWorkCorrectly() {
        // Given
        String relationship = "Parent";

        // When
        guardian.setRelationship(relationship);

        // Then
        assertThat(guardian.getRelationship()).isEqualTo(relationship);
    }

    @Test
    void setAndGetPickupAuthorized_ShouldWorkCorrectly() {
        // Given
        boolean pickupAuthorized = true;

        // When
        guardian.setPickupAuthorized(pickupAuthorized);

        // Then
        assertThat(guardian.getPickupAuthorized()).isEqualTo(pickupAuthorized);
    }

    @Test
    void setAndGetEmergencyContact_ShouldWorkCorrectly() {
        // Given
        String emergencyContact = "555-1234";

        // When
        guardian.setEmergencyContact(emergencyContact);

        // Then
        assertThat(guardian.getEmergencyContact()).isEqualTo(emergencyContact);
    }

    @Test
    void setAndGetCreatedAt_ShouldWorkCorrectly() {
        // Given
        Instant createdAt = Instant.now();

        // When
        guardian.setCreatedAt(createdAt);

        // Then
        assertThat(guardian.getCreatedAt()).isEqualTo(createdAt);
    }

    @Test
    void setAndGetUpdatedAt_ShouldWorkCorrectly() {
        // Given
        Instant updatedAt = Instant.now();

        // When
        guardian.setUpdatedAt(updatedAt);

        // Then
        assertThat(guardian.getUpdatedAt()).isEqualTo(updatedAt);
    }

    @Test
    void newGuardian_ShouldHaveNullValues() {
        // Given
        Guardian newGuardian = new Guardian();

        // Then
        assertThat(newGuardian.getId()).isNull();
        assertThat(newGuardian.getUserId()).isNull();
        assertThat(newGuardian.getRelationship()).isNull();
        assertThat(newGuardian.getPickupAuthorized()).isFalse(); // Default value
        assertThat(newGuardian.getEmergencyContact()).isNull();
        assertThat(newGuardian.getCreatedAt()).isNull();
        assertThat(newGuardian.getUpdatedAt()).isNull();
    }
}
