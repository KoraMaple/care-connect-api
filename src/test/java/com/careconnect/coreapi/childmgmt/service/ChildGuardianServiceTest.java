package com.careconnect.coreapi.childmgmt.service;

import com.careconnect.coreapi.childmgmt.jpa.Child;
import com.careconnect.coreapi.childmgmt.jpa.ChildGuardian;
import com.careconnect.coreapi.childmgmt.jpa.Guardian;
import com.careconnect.coreapi.childmgmt.repository.ChildGuardianRepository;
import com.careconnect.coreapi.childmgmt.repository.ChildRepository;
import com.careconnect.coreapi.childmgmt.repository.GuardianRepository;
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
class ChildGuardianServiceTest {

    @Mock
    private ChildGuardianRepository childGuardianRepository;

    @Mock
    private ChildRepository childRepository;

    @Mock
    private GuardianRepository guardianRepository;

    @InjectMocks
    private ChildGuardianService childGuardianService;

    private Child testChild;
    private Guardian testGuardian;
    private ChildGuardian testChildGuardian;
    private UUID childId;
    private UUID guardianId;

    @BeforeEach
    void setUp() {
        childId = UUID.randomUUID();
        guardianId = UUID.randomUUID();

        testChild = new Child();
        testChild.setId(childId);
        testChild.setFirstName("John");
        testChild.setLastName("Doe");

        testGuardian = new Guardian();
        testGuardian.setId(guardianId);
        testGuardian.setRelationship("Parent");

        testChildGuardian = new ChildGuardian();
        testChildGuardian.setId(UUID.randomUUID());
        testChildGuardian.setChild(testChild);
        testChildGuardian.setGuardian(testGuardian);
        testChildGuardian.setPrimaryGuardian(false);
        testChildGuardian.setCreatedAt(Instant.now());
        testChildGuardian.setUpdatedAt(Instant.now());
    }

    @Test
    void addGuardianToChild_WithValidData_ShouldCreateRelationship() {
        // Given
        when(childRepository.findById(childId)).thenReturn(Optional.of(testChild));
        when(guardianRepository.findById(guardianId)).thenReturn(Optional.of(testGuardian));
        when(childGuardianRepository.existsByChildIdAndGuardianId(childId, guardianId)).thenReturn(false);
        when(childGuardianRepository.save(any(ChildGuardian.class))).thenReturn(testChildGuardian);

        // When
        ChildGuardian result = childGuardianService.addGuardianToChild(childId, guardianId, false);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getChild().getId()).isEqualTo(childId);
        assertThat(result.getGuardian().getId()).isEqualTo(guardianId);
        verify(childRepository).findById(childId);
        verify(guardianRepository).findById(guardianId);
        verify(childGuardianRepository).save(any(ChildGuardian.class));
    }

    @Test
    void addGuardianToChild_WhenChildNotFound_ShouldThrowResourceNotFoundException() {
        // Given
        when(childRepository.findById(childId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> childGuardianService.addGuardianToChild(childId, guardianId, false))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Child not found with ID: " + childId);
        verify(childRepository).findById(childId);
        verifyNoInteractions(guardianRepository, childGuardianRepository);
    }

    @Test
    void addGuardianToChild_WhenGuardianNotFound_ShouldThrowResourceNotFoundException() {
        // Given
        when(childRepository.findById(childId)).thenReturn(Optional.of(testChild));
        when(guardianRepository.findById(guardianId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> childGuardianService.addGuardianToChild(childId, guardianId, false))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Guardian not found with ID: " + guardianId);
        verify(guardianRepository).findById(guardianId);
        verifyNoInteractions(childGuardianRepository);
    }

    @Test
    void addGuardianToChild_WhenRelationshipExists_ShouldThrowDuplicateResourceException() {
        // Given
        when(childRepository.findById(childId)).thenReturn(Optional.of(testChild));
        when(guardianRepository.findById(guardianId)).thenReturn(Optional.of(testGuardian));
        when(childGuardianRepository.existsByChildIdAndGuardianId(childId, guardianId)).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> childGuardianService.addGuardianToChild(childId, guardianId, false))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Guardian is already associated with this child");
        verify(childGuardianRepository).existsByChildIdAndGuardianId(childId, guardianId);
        verify(childGuardianRepository, never()).save(any());
    }

    @Test
    void addGuardianToChild_AsPrimary_WhenPrimaryExists_ShouldThrowValidationException() {
        // Given
        ChildGuardian existingPrimary = new ChildGuardian();
        existingPrimary.setPrimaryGuardian(true);

        when(childRepository.findById(childId)).thenReturn(Optional.of(testChild));
        when(guardianRepository.findById(guardianId)).thenReturn(Optional.of(testGuardian));
        when(childGuardianRepository.existsByChildIdAndGuardianId(childId, guardianId)).thenReturn(false);
        when(childGuardianRepository.findPrimaryGuardianByChildId(childId)).thenReturn(Optional.of(existingPrimary));

        // When & Then
        assertThatThrownBy(() -> childGuardianService.addGuardianToChild(childId, guardianId, true))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Child already has a primary guardian");
        verify(childGuardianRepository).findPrimaryGuardianByChildId(childId);
        verify(childGuardianRepository, never()).save(any());
    }

    @Test
    void getGuardiansForChild_WhenChildExists_ShouldReturnGuardians() {
        // Given
        when(childRepository.existsById(childId)).thenReturn(true);
        when(childGuardianRepository.findByChildId(childId)).thenReturn(List.of(testChildGuardian));

        // When
        List<Guardian> result = childGuardianService.getGuardiansForChild(childId);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(guardianId);
        verify(childRepository).existsById(childId);
        verify(childGuardianRepository).findByChildId(childId);
    }

    @Test
    void getGuardiansForChild_WhenChildNotFound_ShouldThrowResourceNotFoundException() {
        // Given
        when(childRepository.existsById(childId)).thenReturn(false);

        // When & Then
        assertThatThrownBy(() -> childGuardianService.getGuardiansForChild(childId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Child not found with ID: " + childId);
        verify(childRepository).existsById(childId);
        verifyNoInteractions(childGuardianRepository);
    }

    @Test
    void removeGuardianFromChild_WhenMultipleGuardians_ShouldRemoveRelationship() {
        // Given
        when(childGuardianRepository.findByChildIdAndGuardianId(childId, guardianId))
                .thenReturn(Optional.of(testChildGuardian));
        when(childGuardianRepository.countByChildId(childId)).thenReturn(2L);

        // When
        childGuardianService.removeGuardianFromChild(childId, guardianId);

        // Then
        verify(childGuardianRepository).findByChildIdAndGuardianId(childId, guardianId);
        verify(childGuardianRepository).countByChildId(childId);
        verify(childGuardianRepository).delete(testChildGuardian);
    }

    @Test
    void removeGuardianFromChild_WhenLastGuardian_ShouldThrowValidationException() {
        // Given
        when(childGuardianRepository.findByChildIdAndGuardianId(childId, guardianId))
                .thenReturn(Optional.of(testChildGuardian));
        when(childGuardianRepository.countByChildId(childId)).thenReturn(1L);

        // When & Then
        assertThatThrownBy(() -> childGuardianService.removeGuardianFromChild(childId, guardianId))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Cannot remove the last guardian from a child");
        verify(childGuardianRepository).countByChildId(childId);
        verify(childGuardianRepository, never()).delete(any());
    }

    @Test
    void updatePrimaryGuardian_ShouldUpdatePrimaryStatus() {
        // Given
        ChildGuardian existingPrimary = new ChildGuardian();
        existingPrimary.setPrimaryGuardian(true);

        when(childGuardianRepository.findByChildIdAndGuardianId(childId, guardianId))
                .thenReturn(Optional.of(testChildGuardian));
        when(childGuardianRepository.findPrimaryGuardianByChildId(childId))
                .thenReturn(Optional.of(existingPrimary));
        when(childGuardianRepository.save(any(ChildGuardian.class))).thenReturn(testChildGuardian);

        // When
        ChildGuardian result = childGuardianService.updatePrimaryGuardian(childId, guardianId);

        // Then
        assertThat(result).isNotNull();
        verify(childGuardianRepository).findByChildIdAndGuardianId(childId, guardianId);
        verify(childGuardianRepository).findPrimaryGuardianByChildId(childId);
        verify(childGuardianRepository, times(2)).save(any(ChildGuardian.class));
    }
}
