package com.careconnect.coreapi.childmgmt.service;

import com.careconnect.coreapi.childmgmt.domain.Allergy;
import com.careconnect.coreapi.childmgmt.domain.Child;
import com.careconnect.coreapi.childmgmt.domain.ChildAllergy;
import com.careconnect.coreapi.childmgmt.internal.repository.AllergyRepository;
import com.careconnect.coreapi.childmgmt.internal.repository.ChildAllergyRepository;
import com.careconnect.coreapi.childmgmt.internal.repository.ChildRepository;
import com.careconnect.coreapi.childmgmt.internal.service.AllergyService;
import com.careconnect.coreapi.common.exceptions.DuplicateResourceException;
import com.careconnect.coreapi.common.exceptions.ResourceNotFoundException;
import com.careconnect.coreapi.common.exceptions.ValidationException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AllergyServiceTest {

    @Mock
    private AllergyRepository allergyRepository;

    @Mock
    private ChildAllergyRepository childAllergyRepository;

    @Mock
    private ChildRepository childRepository;

    @InjectMocks
    private AllergyService allergyService;

    private Allergy testAllergy;
    private Child testChild;
    private ChildAllergy testChildAllergy;
    private UUID allergyId;
    private UUID childId;

    @BeforeEach
    void setUp() {
        allergyId = UUID.randomUUID();
        childId = UUID.randomUUID();

        testAllergy = new Allergy();
        testAllergy.setId(allergyId);
        testAllergy.setName("Peanuts");
        testAllergy.setDescription("Peanut allergy");
        testAllergy.setCreatedAt(Instant.now());
        testAllergy.setUpdatedAt(Instant.now());

        testChild = new Child();
        testChild.setId(childId);
        testChild.setFirstName("John");
        testChild.setLastName("Doe");
        testChild.setCreatedAt(Instant.now());

        testChildAllergy = new ChildAllergy();
        testChildAllergy.setChild(testChild);
        testChildAllergy.setAllergy(testAllergy);
        testChildAllergy.setNotes("Severe reaction");
        testChildAllergy.setCreatedAt(Instant.now());
        testChildAllergy.setUpdatedAt(Instant.now());
    }

    @Test
    void getAllAllergies_ShouldReturnAllAllergies() {
        // Given
        when(allergyRepository.findAll()).thenReturn(List.of(testAllergy));

        // When
        List<Allergy> result = allergyService.getAllAllergies();

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Peanuts");
        verify(allergyRepository).findAll();
    }

    @Test
    void getAllergyById_WhenAllergyExists_ShouldReturnAllergy() {
        // Given
        when(allergyRepository.findById(allergyId)).thenReturn(Optional.of(testAllergy));

        // When
        Allergy result = allergyService.getAllergyById(allergyId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Peanuts");
        verify(allergyRepository).findById(allergyId);
    }

    @Test
    void getAllergyById_WhenAllergyDoesNotExist_ShouldThrowResourceNotFoundException() {
        // Given
        when(allergyRepository.findById(allergyId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> allergyService.getAllergyById(allergyId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Allergy not found with ID: " + allergyId);
        verify(allergyRepository).findById(allergyId);
    }

    @Test
    void createAllergy_WithValidData_ShouldCreateAllergy() {
        // Given
        Allergy newAllergy = new Allergy();
        newAllergy.setName("Shellfish");
        newAllergy.setDescription("Shellfish allergy");

        when(allergyRepository.findByNameIgnoreCase("Shellfish")).thenReturn(Optional.empty());
        when(allergyRepository.save(any(Allergy.class))).thenReturn(testAllergy);

        // When
        Allergy result = allergyService.createAllergy(newAllergy);

        // Then
        assertThat(result).isNotNull();
        verify(allergyRepository).findByNameIgnoreCase("Shellfish");
        verify(allergyRepository).save(any(Allergy.class));
    }

    @Test
    void createAllergy_WithDuplicateName_ShouldThrowDuplicateResourceException() {
        // Given
        Allergy newAllergy = new Allergy();
        newAllergy.setName("Peanuts");

        when(allergyRepository.findByNameIgnoreCase("Peanuts")).thenReturn(Optional.of(testAllergy));

        // When & Then
        assertThatThrownBy(() -> allergyService.createAllergy(newAllergy))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Allergy with name 'Peanuts' already exists");
        verify(allergyRepository).findByNameIgnoreCase("Peanuts");
        verifyNoMoreInteractions(allergyRepository);
    }

    @Test
    void createAllergy_WithEmptyName_ShouldThrowValidationException() {
        // Given
        Allergy newAllergy = new Allergy();
        newAllergy.setName("");

        // When & Then
        assertThatThrownBy(() -> allergyService.createAllergy(newAllergy))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Allergy name is required");
        verifyNoInteractions(allergyRepository);
    }

    @Test
    void createAllergy_WithNullName_ShouldThrowValidationException() {
        // Given
        Allergy newAllergy = new Allergy();
        newAllergy.setName(null);

        // When & Then
        assertThatThrownBy(() -> allergyService.createAllergy(newAllergy))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Allergy name is required");
        verifyNoInteractions(allergyRepository);
    }

    @Test
    void addAllergyToChild_WithValidData_ShouldCreateChildAllergy() {
        // Given
        when(childRepository.findById(childId)).thenReturn(Optional.of(testChild));
        when(allergyRepository.findById(allergyId)).thenReturn(Optional.of(testAllergy));
        when(childAllergyRepository.existsByChildIdAndAllergyId(childId, allergyId)).thenReturn(false);
        when(childAllergyRepository.save(any(ChildAllergy.class))).thenReturn(testChildAllergy);

        // When
        ChildAllergy result = allergyService.addAllergyToChild(childId, allergyId, "Severe reaction");

        // Then
        assertThat(result).isNotNull();
        verify(childRepository).findById(childId);
        verify(allergyRepository).findById(allergyId);
        verify(childAllergyRepository).existsByChildIdAndAllergyId(childId, allergyId);
        verify(childAllergyRepository).save(any(ChildAllergy.class));
    }

    @Test
    void addAllergyToChild_WhenChildNotFound_ShouldThrowResourceNotFoundException() {
        // Given
        when(childRepository.findById(childId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> allergyService.addAllergyToChild(childId, allergyId, "Notes"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Child not found with ID: " + childId);
        verify(childRepository).findById(childId);
        verifyNoMoreInteractions(allergyRepository, childAllergyRepository);
    }

    @Test
    void addAllergyToChild_WhenAllergyNotFound_ShouldThrowResourceNotFoundException() {
        // Given
        when(childRepository.findById(childId)).thenReturn(Optional.of(testChild));
        when(allergyRepository.findById(allergyId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> allergyService.addAllergyToChild(childId, allergyId, "Notes"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Allergy not found with ID: " + allergyId);
        verify(childRepository).findById(childId);
        verify(allergyRepository).findById(allergyId);
        verifyNoInteractions(childAllergyRepository);
    }

    @Test
    void addAllergyToChild_WhenRelationshipExists_ShouldThrowDuplicateResourceException() {
        // Given
        when(childRepository.findById(childId)).thenReturn(Optional.of(testChild));
        when(allergyRepository.findById(allergyId)).thenReturn(Optional.of(testAllergy));
        when(childAllergyRepository.existsByChildIdAndAllergyId(childId, allergyId)).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> allergyService.addAllergyToChild(childId, allergyId, "Notes"))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Allergy is already assigned to this child");
        verify(childAllergyRepository).existsByChildIdAndAllergyId(childId, allergyId);
        verify(childAllergyRepository, never()).save(any());
    }

    @Test
    void getAllergiesForChild_WhenChildExists_ShouldReturnAllergies() {
        // Given
        when(childRepository.existsById(childId)).thenReturn(true);
        when(childAllergyRepository.findByChildId(childId)).thenReturn(List.of(testChildAllergy));

        // When
        List<ChildAllergy> result = allergyService.getAllergiesForChild(childId);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getAllergy().getName()).isEqualTo("Peanuts");
        verify(childRepository).existsById(childId);
        verify(childAllergyRepository).findByChildId(childId);
    }

    @Test
    void getAllergiesForChild_WhenChildNotFound_ShouldThrowResourceNotFoundException() {
        // Given
        when(childRepository.existsById(childId)).thenReturn(false);

        // When & Then
        assertThatThrownBy(() -> allergyService.getAllergiesForChild(childId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Child not found with ID: " + childId);
        verify(childRepository).existsById(childId);
        verifyNoInteractions(childAllergyRepository);
    }

    @Test
    void removeAllergyFromChild_WhenRelationshipExists_ShouldRemoveAllergy() {
        // Given
        when(childAllergyRepository.existsByChildIdAndAllergyId(childId, allergyId)).thenReturn(true);

        // When
        allergyService.removeAllergyFromChild(childId, allergyId);

        // Then
        verify(childAllergyRepository).existsByChildIdAndAllergyId(childId, allergyId);
        verify(childAllergyRepository).deleteByChildIdAndAllergyId(childId, allergyId);
    }

    @Test
    void removeAllergyFromChild_WhenRelationshipNotFound_ShouldThrowResourceNotFoundException() {
        // Given
        when(childAllergyRepository.existsByChildIdAndAllergyId(childId, allergyId)).thenReturn(false);

        // When & Then
        assertThatThrownBy(() -> allergyService.removeAllergyFromChild(childId, allergyId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Allergy relationship not found");
        verify(childAllergyRepository).existsByChildIdAndAllergyId(childId, allergyId);
        verify(childAllergyRepository, never()).deleteByChildIdAndAllergyId(any(), any());
    }

    @Test
    void searchAllergiesByName_ShouldReturnMatchingAllergies() {
        // Given
        when(allergyRepository.findByNameContainingIgnoreCase("Pea")).thenReturn(List.of(testAllergy));

        // When
        List<Allergy> result = allergyService.searchAllergiesByName("Pea");

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Peanuts");
        verify(allergyRepository).findByNameContainingIgnoreCase("Pea");
    }
}