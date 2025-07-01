package com.careconnect.coreapi.childmgmt.repository;

import com.careconnect.coreapi.childmgmt.jpa.Child;
import com.careconnect.coreapi.childmgmt.jpa.Guardian;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ChildRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ChildRepository childRepository;

    private Child testChild1;
    private Child testChild2;
    private Guardian testGuardian;

    @BeforeEach
    void setUp() {
        testGuardian = new Guardian();
        testGuardian.setRelationship("Parent");
        testGuardian.setEmergencyContact("123-456-7890");
        testGuardian.setPickupAuthorized(true);
        testGuardian.setCreatedAt(Instant.now());
        testGuardian.setUpdatedAt(Instant.now());
        entityManager.persistAndFlush(testGuardian);

        testChild1 = new Child();
        testChild1.setFirstName("John");
        testChild1.setLastName("Doe");
        testChild1.setDob(Instant.parse("2020-01-01T00:00:00Z"));
        testChild1.setGender("Male");
        testChild1.setGuardian(testGuardian);
        testChild1.setCreatedAt(Instant.now());
        testChild1.setUpdatedAt(Instant.now());
        entityManager.persistAndFlush(testChild1);

        testChild2 = new Child();
        testChild2.setFirstName("Jane");
        testChild2.setLastName("Smith");
        testChild2.setDob(Instant.parse("2021-01-01T00:00:00Z"));
        testChild2.setGender("Female");
        testChild2.setCreatedAt(Instant.now());
        testChild2.setUpdatedAt(Instant.now());
        entityManager.persistAndFlush(testChild2);

        entityManager.clear();
    }

    @Test
    void findByFilters_WithNameFilter_ShouldReturnMatchingChildren() {
        // When
        Page<Child> result = childRepository.findByFilters("John", null, PageRequest.of(0, 10));

        // Then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getFirstName()).isEqualTo("John");
    }

    @Test
    void findByFilters_WithGenderFilter_ShouldReturnMatchingChildren() {
        // When
        Page<Child> result = childRepository.findByFilters(null, "Female", PageRequest.of(0, 10));

        // Then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getGender()).isEqualTo("Female");
    }

    @Test
    void findByFilters_WithBothFilters_ShouldReturnMatchingChildren() {
        // When
        Page<Child> result = childRepository.findByFilters("John", "Male", PageRequest.of(0, 10));

        // Then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getFirstName()).isEqualTo("John");
        assertThat(result.getContent().get(0).getGender()).isEqualTo("Male");
    }

    @Test
    void findByFilters_WithNoFilters_ShouldReturnAllChildren() {
        // When
        Page<Child> result = childRepository.findByFilters(null, null, PageRequest.of(0, 10));

        // Then
        assertThat(result.getContent()).hasSize(2);
    }

    @Test
    void findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase_ShouldReturnMatchingChildren() {
        // When
        List<Child> result = childRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase("john", "smith");

        // Then
        assertThat(result).hasSize(2); // John Doe and Jane Smith
    }

    @Test
    void findByGuardianId_ShouldReturnChildrenWithGuardian() {
        // When
        List<Child> result = childRepository.findByGuardianId(testGuardian.getId());

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFirstName()).isEqualTo("John");
    }

    @Test
    void save_ShouldPersistChild() {
        // Given
        Child newChild = new Child();
        newChild.setFirstName("Alice");
        newChild.setLastName("Johnson");
        newChild.setDob(Instant.parse("2019-01-01T00:00:00Z"));
        newChild.setGender("Female");
        newChild.setCreatedAt(Instant.now());
        newChild.setUpdatedAt(Instant.now());

        // When
        Child saved = childRepository.save(newChild);

        // Then
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getFirstName()).isEqualTo("Alice");

        Optional<Child> found = childRepository.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getFirstName()).isEqualTo("Alice");
    }

    @Test
    void deleteById_ShouldRemoveChild() {
        // Given
        Long initialCount = childRepository.count();

        // When
        childRepository.deleteById(testChild1.getId());

        // Then
        assertThat(childRepository.count()).isEqualTo(initialCount - 1);
        assertThat(childRepository.findById(testChild1.getId())).isEmpty();
    }
}
