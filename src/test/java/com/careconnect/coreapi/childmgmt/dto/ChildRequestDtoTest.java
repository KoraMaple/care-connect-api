package com.careconnect.coreapi.childmgmt.dto;

import com.careconnect.coreapi.childmgmt.domain.Child;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ChildRequestDtoTest {

    private ChildRequestDto dto;

    @BeforeEach
    void setUp() {
        dto = new ChildRequestDto();
    }

    @Test
    void builder_ShouldCreateDtoWithValues() {
        // Given
        String firstName = "John";
        String lastName = "Doe";
        Instant dob = Instant.parse("2020-01-01T00:00:00Z");
        String gender = "Male";

        // When
        ChildRequestDto builtDto = ChildRequestDto.builder()
                .firstName(firstName)
                .lastName(lastName)
                .dob(dob)
                .gender(gender)
                .build();

        // Then
        assertThat(builtDto.getFirstName()).isEqualTo(firstName);
        assertThat(builtDto.getLastName()).isEqualTo(lastName);
        assertThat(builtDto.getDob()).isEqualTo(dob);
        assertThat(builtDto.getGender()).isEqualTo(gender);
    }

    @Test
    void setAndGetFirstName_ShouldWorkCorrectly() {
        // Given
        String firstName = "John";

        // When
        dto.setFirstName(firstName);

        // Then
        assertThat(dto.getFirstName()).isEqualTo(firstName);
    }

    @Test
    void setAndGetLastName_ShouldWorkCorrectly() {
        // Given
        String lastName = "Doe";

        // When
        dto.setLastName(lastName);

        // Then
        assertThat(dto.getLastName()).isEqualTo(lastName);
    }

    @Test
    void setAndGetDob_ShouldWorkCorrectly() {
        // Given
        Instant dob = Instant.parse("2020-01-01T00:00:00Z");

        // When
        dto.setDob(dob);

        // Then
        assertThat(dto.getDob()).isEqualTo(dob);
    }

    @Test
    void setAndGetGender_ShouldWorkCorrectly() {
        // Given
        String gender = "Male";

        // When
        dto.setGender(gender);

        // Then
        assertThat(dto.getGender()).isEqualTo(gender);
    }

    @Test
    void toEntity_ShouldCreateChildEntity() {
        // Given
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setDob(Instant.parse("2020-01-01T00:00:00Z"));
        dto.setGender("Male");
        dto.setSpecialNeeds("None");
        dto.setEmergencyContact("555-1234");

        // When
        Child child = dto.toEntity();

        // Then
        assertThat(child.getFirstName()).isEqualTo("John");
        assertThat(child.getLastName()).isEqualTo("Doe");
        assertThat(child.getDob()).isEqualTo(Instant.parse("2020-01-01T00:00:00Z"));
        assertThat(child.getGender()).isEqualTo("Male");
        assertThat(child.getSpecialNeeds()).isEqualTo("None");
        assertThat(child.getEmergencyContact()).isEqualTo("555-1234");
    }

    @Test
    void updateEntity_ShouldUpdateExistingChild() {
        // Given
        Child existingChild = new Child();
        existingChild.setId(UUID.randomUUID());
        existingChild.setFirstName("OldName");
        
        dto.setFirstName("NewName");
        dto.setLastName("Doe");
        dto.setDob(Instant.parse("2020-01-01T00:00:00Z"));
        dto.setGender("Male");

        // When
        Child updatedChild = dto.updateEntity(existingChild);

        // Then
        assertThat(updatedChild.getId()).isEqualTo(existingChild.getId()); // ID should remain
        assertThat(updatedChild.getFirstName()).isEqualTo("NewName");
        assertThat(updatedChild.getLastName()).isEqualTo("Doe");
        assertThat(updatedChild.getDob()).isEqualTo(Instant.parse("2020-01-01T00:00:00Z"));
        assertThat(updatedChild.getGender()).isEqualTo("Male");
    }

    @Test
    void allArgsConstructor_ShouldCreateDtoWithAllFields() {
        // Given
        String firstName = "John";
        String lastName = "Doe";
        Instant dob = Instant.parse("2020-01-01T00:00:00Z");
        String gender = "Male";
        String specialNeeds = "None";
        String emergencyContact = "555-1234";
        UUID guardianId = UUID.randomUUID();

        // When
        ChildRequestDto createdDto = new ChildRequestDto(firstName, lastName, dob, gender, specialNeeds, emergencyContact, guardianId);

        // Then
        assertThat(createdDto.getFirstName()).isEqualTo(firstName);
        assertThat(createdDto.getLastName()).isEqualTo(lastName);
        assertThat(createdDto.getDob()).isEqualTo(dob);
        assertThat(createdDto.getGender()).isEqualTo(gender);
        assertThat(createdDto.getSpecialNeeds()).isEqualTo(specialNeeds);
        assertThat(createdDto.getEmergencyContact()).isEqualTo(emergencyContact);
        assertThat(createdDto.getGuardianId()).isEqualTo(guardianId);
    }
}
