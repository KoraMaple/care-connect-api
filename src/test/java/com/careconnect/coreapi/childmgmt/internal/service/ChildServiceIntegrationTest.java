package com.careconnect.coreapi.childmgmt.internal.service;

import com.careconnect.coreapi.childmgmt.domain.Child;
import com.careconnect.coreapi.childmgmt.internal.repository.ChildRepository;
import com.careconnect.coreapi.childmgmt.internal.repository.GuardianRepository;
import com.careconnect.coreapi.common.exceptions.ResourceNotFoundException;
import com.careconnect.coreapi.common.response.PageResponse;
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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
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
    private UUID childId;

    @BeforeEach
    void setUp() {
        childId = UUID.randomUUID();
        
        testChild = new Child();
        testChild.setId(childId);
        testChild.setFirstName("John");
        testChild.setLastName("Doe");
        testChild.setDob(Instant.parse("2020-01-01T00:00:00Z"));
        testChild.setGender("Male");
        testChild.setCreatedAt(Instant.now());
        testChild.setUpdatedAt(Instant.now());
    }

    @Test
    void getAllChildren_ShouldReturnPageResponse() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Child> childrenPage = new PageImpl<>(Arrays.asList(testChild), pageable, 1);
        when(childRepository.findAll(pageable)).thenReturn(childrenPage);

        // When
        PageResponse<Child> result = childService.getAllChildren(pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getData()).hasSize(1);
        assertThat(result.getData().get(0).getFirstName()).isEqualTo("John");
        verify(childRepository).findAll(pageable);
    }

    @Test
    void getChildById_WhenChildExists_ShouldReturnChild() {
        // Given
        when(childRepository.findById(childId)).thenReturn(Optional.of(testChild));

        // When
        Child result = childService.getChildById(childId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(childId);
        assertThat(result.getFirstName()).isEqualTo("John");
        verify(childRepository).findById(childId);
    }

    @Test
    void getChildById_WhenChildNotExists_ShouldThrowException() {
        // Given
        when(childRepository.findById(childId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> childService.getChildById(childId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Child not found with ID: " + childId);
        
        verify(childRepository).findById(childId);
    }

    @Test
    void createChild_WithValidData_ShouldSaveChild() {
        // Given
        Child newChild = new Child();
        newChild.setFirstName("Jane");
        newChild.setLastName("Smith");
        newChild.setDob(Instant.parse("2021-01-01T00:00:00Z"));
        newChild.setGender("Female");

        when(childRepository.save(any(Child.class))).thenReturn(testChild);

        // When
        Child result = childService.createChild(newChild);

        // Then
        assertThat(result).isNotNull();
        assertThat(newChild.getId()).isNull(); // Should reset ID for new entity
        assertThat(newChild.getCreatedAt()).isNotNull();
        assertThat(newChild.getUpdatedAt()).isNotNull();
        verify(childRepository).save(any(Child.class));
    }

    @Test
    void updateChild_WhenChildExists_ShouldUpdateChild() {
        // Given
        Child updatedData = new Child();
        updatedData.setFirstName("UpdatedName");
        updatedData.setLastName("UpdatedLastName");
        updatedData.setDob(Instant.parse("2020-02-01T00:00:00Z"));
        updatedData.setGender("Female");

        when(childRepository.findById(childId)).thenReturn(Optional.of(testChild));
        when(childRepository.save(any(Child.class))).thenReturn(testChild);

        // When
        Child result = childService.updateChild(childId, updatedData);

        // Then
        assertThat(result).isNotNull();
        verify(childRepository).findById(childId);
        verify(childRepository).save(any(Child.class));
    }

    @Test
    void updateChild_WhenChildNotExists_ShouldThrowException() {
        // Given
        Child updatedData = new Child();
        when(childRepository.findById(childId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> childService.updateChild(childId, updatedData))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Child not found with ID: " + childId);
        
        verify(childRepository).findById(childId);
        verify(childRepository, never()).save(any(Child.class));
    }

    @Test
    void deleteChild_WhenChildExists_ShouldDeleteChild() {
        // Given
        when(childRepository.existsById(childId)).thenReturn(true);
        doNothing().when(childRepository).deleteById(childId);

        // When
        childService.deleteChild(childId);

        // Then
        verify(childRepository).existsById(childId);
        verify(childRepository).deleteById(childId);
    }

    @Test
    void deleteChild_WhenChildNotExists_ShouldThrowException() {
        // Given
        when(childRepository.existsById(childId)).thenReturn(false);

        // When & Then
        assertThatThrownBy(() -> childService.deleteChild(childId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Child not found with ID: " + childId);
        
        verify(childRepository).existsById(childId);
        verify(childRepository, never()).deleteById(childId);
    }

    @Test
    void searchChildren_ShouldReturnFilteredResults() {
        // Given
        String name = "John";
        String gender = "Male";
        Pageable pageable = PageRequest.of(0, 10);
        Page<Child> childrenPage = new PageImpl<>(Arrays.asList(testChild), pageable, 1);
        
        when(childRepository.findByFilters(name, gender, pageable)).thenReturn(childrenPage);

        // When
        Page<Child> result = childService.searchChildren(name, gender, pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getFirstName()).isEqualTo("John");
        verify(childRepository).findByFilters(name, gender, pageable);
    }

    @Test
    void getChildrenByGuardian_ShouldReturnChildrenList() {
        // Given
        UUID guardianId = UUID.randomUUID();
        when(guardianRepository.existsById(guardianId)).thenReturn(true);
        when(childRepository.findByAssociatedGuardianId(guardianId)).thenReturn(Arrays.asList(testChild));

        // When
        List<Child> result = childService.getChildrenByGuardian(guardianId);

        // Then
        assertThat(result)
                .isNotNull()
                .hasSize(1)
                .first()
                .extracting(Child::getFirstName)
                .isEqualTo("John");
        verify(guardianRepository).existsById(guardianId);
        verify(childRepository).findByAssociatedGuardianId(guardianId);
    }
}
