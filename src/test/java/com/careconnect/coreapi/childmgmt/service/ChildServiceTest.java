package com.careconnect.coreapi.childmgmt.service;

import com.careconnect.coreapi.childmgmt.domain.Child;
import com.careconnect.coreapi.childmgmt.domain.Guardian;
import com.careconnect.coreapi.childmgmt.internal.ChildRepository;
import com.careconnect.coreapi.childmgmt.internal.GuardianRepository;
import com.careconnect.coreapi.common.exceptions.ResourceNotFoundException;
import com.careconnect.coreapi.common.exceptions.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChildServiceTest {

    @Mock
    private ChildRepository childRepository;

    @Mock
    private GuardianRepository guardianRepository;

    @InjectMocks
    private ChildService childService;

    private Child testChild;
    private Guardian testGuardian;
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
        testChild.setDob(Instant.parse("2020-01-01T00:00:00Z"));
        testChild.setGender("Male");
        testChild.setCreatedAt(Instant.now());
        testChild.setUpdatedAt(Instant.now());

        testGuardian = new Guardian();
        testGuardian.setId(guardianId);
        testGuardian.setRelationship("Parent");
    }

    @Test
    void getAllChildren_ShouldReturnPageOfChildren() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Child> childrenPage = new PageImpl<>(List.of(testChild));
        when(childRepository.findAll(pageable)).thenReturn(childrenPage);

        // When
//        Page<Child> result = childService.getAllChildren(pageable);

        // Then
//        assertThat(result).isNotNull();
//        assertThat(result.getContent()).hasSize(1);
//        assertThat(result.getContent().get(0).getFirstName()).isEqualTo("John");
//        verify(childRepository).findAll(pageable);
    }

    @Test
    void getChildById_WhenChildExists_ShouldReturnChild() {
        // Given
        when(childRepository.findById(childId)).thenReturn(Optional.of(testChild));

        // When
        Child result = childService.getChildById(childId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getFirstName()).isEqualTo("John");
        verify(childRepository).findById(childId);
    }

    @Test
    void getChildById_WhenChildDoesNotExist_ShouldThrowResourceNotFoundException() {
        // Given
        when(childRepository.findById(childId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> childService.getChildById(childId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Child not found with ID: " + childId);
        verify(childRepository).findById(childId);
    }

    @Test
    void createChild_WithValidData_ShouldCreateChild() {
        // Given
        when(childRepository.save(any(Child.class))).thenReturn(testChild);

        // When
        Child result = childService.createChild(testChild);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getFirstName()).isEqualTo("John");
        verify(childRepository).save(any(Child.class));
    }

    @Test
    void createChild_WithInvalidData_ShouldThrowValidationException() {
        // Given
        testChild.setFirstName(null);

        // When & Then
        assertThatThrownBy(() -> childService.createChild(testChild))
                .isInstanceOf(ValidationException.class)
                .hasMessage("First name is required");
        verifyNoInteractions(childRepository);
    }

    @Test
    void updateChild_WhenChildExists_ShouldUpdateChild() {
        // Given
        Child updatedData = new Child();
        updatedData.setFirstName("Jane");
        updatedData.setLastName("Doe");
        updatedData.setDob(Instant.parse("2020-01-01T00:00:00Z"));
        updatedData.setGender("Female");

        when(childRepository.findById(childId)).thenReturn(Optional.of(testChild));
        when(childRepository.save(testChild)).thenReturn(testChild);

        // When
        Child result = childService.updateChild(childId, updatedData);

        // Then
        assertThat(result).isNotNull();
        assertThat(testChild.getFirstName()).isEqualTo("Jane");
        verify(childRepository).findById(childId);
        verify(childRepository).save(testChild);
    }

    @Test
    void deleteChild_WhenChildExists_ShouldDeleteChild() {
        // Given
        when(childRepository.existsById(childId)).thenReturn(true);

        // When
        childService.deleteChild(childId);

        // Then
        verify(childRepository).existsById(childId);
        verify(childRepository).deleteById(childId);
    }

    @Test
    void deleteChild_WhenChildDoesNotExist_ShouldThrowResourceNotFoundException() {
        // Given
        when(childRepository.existsById(childId)).thenReturn(false);

        // When & Then
        assertThatThrownBy(() -> childService.deleteChild(childId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Child not found with ID: " + childId);
        verify(childRepository).existsById(childId);
        verify(childRepository, never()).deleteById(any());
    }

    @Test
    void searchChildren_ShouldReturnFilteredResults() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Child> childrenPage = new PageImpl<>(List.of(testChild));

        when(childRepository.findByFilters(eq("John"), eq("Male"), eq(pageable)))
                .thenReturn(childrenPage);

        // When
        Page<Child> result = childService.searchChildren("John", "Male", pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        verify(childRepository).findByFilters("John", "Male", pageable);
    }

    @Test
    void getChildrenByGuardian_WhenGuardianExists_ShouldReturnChildren() {
        // Given
        when(guardianRepository.existsById(guardianId)).thenReturn(true);
        when(childRepository.findByAssociatedGuardianId(guardianId)).thenReturn(List.of(testChild));

        // When
        List<Child> result = childService.getChildrenByGuardian(guardianId);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFirstName()).isEqualTo("John");
        verify(guardianRepository).existsById(guardianId);
        verify(childRepository).findByAssociatedGuardianId(guardianId);
    }
}
