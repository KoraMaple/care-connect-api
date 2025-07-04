package com.careconnect.coreapi.childmgmt.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ChildTest {

    private Child child;

    @BeforeEach
    void setUp() {
        child = new Child();
    }

    @Test
    void setAndGetId_ShouldWorkCorrectly() {
        // Given
        UUID id = UUID.randomUUID();

        // When
        child.setId(id);

        // Then
        assertThat(child.getId()).isEqualTo(id);
    }

    @Test
    void setAndGetFirstName_ShouldWorkCorrectly() {
        // Given
        String firstName = "John";

        // When
        child.setFirstName(firstName);

        // Then
        assertThat(child.getFirstName()).isEqualTo(firstName);
    }

    @Test
    void setAndGetLastName_ShouldWorkCorrectly() {
        // Given
        String lastName = "Doe";

        // When
        child.setLastName(lastName);

        // Then
        assertThat(child.getLastName()).isEqualTo(lastName);
    }

    @Test
    void setAndGetDob_ShouldWorkCorrectly() {
        // Given
        Instant dob = Instant.parse("2020-01-01T00:00:00Z");

        // When
        child.setDob(dob);

        // Then
        assertThat(child.getDob()).isEqualTo(dob);
    }

    @Test
    void setAndGetGender_ShouldWorkCorrectly() {
        // Given
        String gender = "Male";

        // When
        child.setGender(gender);

        // Then
        assertThat(child.getGender()).isEqualTo(gender);
    }

    @Test
    void setAndGetSpecialNeeds_ShouldWorkCorrectly() {
        // Given
        String specialNeeds = "Allergic to peanuts";

        // When
        child.setSpecialNeeds(specialNeeds);

        // Then
        assertThat(child.getSpecialNeeds()).isEqualTo(specialNeeds);
    }

    @Test
    void setAndGetEmergencyContact_ShouldWorkCorrectly() {
        // Given
        String emergencyContact = "555-1234";

        // When
        child.setEmergencyContact(emergencyContact);

        // Then
        assertThat(child.getEmergencyContact()).isEqualTo(emergencyContact);
    }

    @Test
    void setAndGetCreatedAt_ShouldWorkCorrectly() {
        // Given
        Instant createdAt = Instant.now();

        // When
        child.setCreatedAt(createdAt);

        // Then
        assertThat(child.getCreatedAt()).isEqualTo(createdAt);
    }

    @Test
    void setAndGetUpdatedAt_ShouldWorkCorrectly() {
        // Given
        Instant updatedAt = Instant.now();

        // When
        child.setUpdatedAt(updatedAt);

        // Then
        assertThat(child.getUpdatedAt()).isEqualTo(updatedAt);
    }

    @Test
    void newChild_ShouldHaveNullValues() {
        // Given
        Child newChild = new Child();

        // Then
        assertThat(newChild.getId()).isNull();
        assertThat(newChild.getFirstName()).isNull();
        assertThat(newChild.getLastName()).isNull();
        assertThat(newChild.getDob()).isNull();
        assertThat(newChild.getGender()).isNull();
        assertThat(newChild.getSpecialNeeds()).isNull();
        assertThat(newChild.getEmergencyContact()).isNull();
        assertThat(newChild.getCreatedAt()).isNull();
        assertThat(newChild.getUpdatedAt()).isNull();
    }
}
