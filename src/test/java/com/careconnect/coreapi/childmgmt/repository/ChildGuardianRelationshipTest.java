package com.careconnect.coreapi.childmgmt.repository;

import com.careconnect.coreapi.childmgmt.domain.Child;
import com.careconnect.coreapi.childmgmt.domain.ChildGuardian;
import com.careconnect.coreapi.childmgmt.domain.Guardian;
import com.careconnect.coreapi.user.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class ChildGuardianRelationshipTest {

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testMultipleGuardians() {
        // Create users
        User user1 = new User();
        user1.setClerkUserId("user1");
        user1.setCreatedAt(Instant.now());
        user1.setUpdatedAt(Instant.now());
        entityManager.persistAndFlush(user1);

        User user2 = new User();
        user2.setClerkUserId("user2");
        user2.setCreatedAt(Instant.now());
        user2.setUpdatedAt(Instant.now());
        entityManager.persistAndFlush(user2);

        User user3 = new User();
        user3.setClerkUserId("user3");
        user3.setCreatedAt(Instant.now());
        user3.setUpdatedAt(Instant.now());
        entityManager.persistAndFlush(user3);

        // Create guardians
        Guardian guardian1 = new Guardian();
        guardian1.setUserId(user1.getId());
        guardian1.setRelationship("Mother");
        guardian1.setPickupAuthorized(true);
        guardian1.setCreatedAt(Instant.now());
        guardian1.setUpdatedAt(Instant.now());
        entityManager.persistAndFlush(guardian1);

        Guardian guardian2 = new Guardian();
        guardian2.setUserId(user2.getId());
        guardian2.setRelationship("Father");
        guardian2.setPickupAuthorized(true);
        guardian2.setCreatedAt(Instant.now());
        guardian2.setUpdatedAt(Instant.now());
        entityManager.persistAndFlush(guardian2);

        Guardian guardian3 = new Guardian();
        guardian3.setUserId(user3.getId());
        guardian3.setRelationship("Grandmother");
        guardian3.setPickupAuthorized(false);
        guardian3.setCreatedAt(Instant.now());
        guardian3.setUpdatedAt(Instant.now());
        entityManager.persistAndFlush(guardian3);

        // Create child with primary guardian (legacy relationship)
        Child child = new Child();
        child.setFirstName("Alice");
        child.setLastName("Johnson");
        child.setDob(Instant.parse("2018-05-15T00:00:00Z"));
        child.setGender("Female");
        child.setGuardian(guardian1); // Set primary guardian (legacy)
        child.setCreatedAt(Instant.now());
        child.setUpdatedAt(Instant.now());
        entityManager.persistAndFlush(child);

        // Create child-guardian relationships
        ChildGuardian childGuardian1 = new ChildGuardian();
        childGuardian1.setChild(child);
        childGuardian1.setGuardian(guardian1);
        childGuardian1.setPrimaryGuardian(true); // Mother is primary
        childGuardian1.setCreatedAt(Instant.now());
        childGuardian1.setUpdatedAt(Instant.now());
        entityManager.persistAndFlush(childGuardian1);

        ChildGuardian childGuardian2 = new ChildGuardian();
        childGuardian2.setChild(child);
        childGuardian2.setGuardian(guardian2);
        childGuardian2.setPrimaryGuardian(false); // Father is secondary
        childGuardian2.setCreatedAt(Instant.now());
        childGuardian2.setUpdatedAt(Instant.now());
        entityManager.persistAndFlush(childGuardian2);

        ChildGuardian childGuardian3 = new ChildGuardian();
        childGuardian3.setChild(child);
        childGuardian3.setGuardian(guardian3);
        childGuardian3.setPrimaryGuardian(false); // Grandmother is secondary
        childGuardian3.setCreatedAt(Instant.now());
        childGuardian3.setUpdatedAt(Instant.now());
        entityManager.persistAndFlush(childGuardian3);

        entityManager.flush();
        entityManager.clear();

        // Retrieve the child and test the new methods
        Child retrievedChild = entityManager.find(Child.class, child.getId());
        
        // Test legacy method (should still work)
        assertThat(retrievedChild.getGuardianId()).isEqualTo(guardian1.getId());
        
        // Test new methods for multiple guardians
        List<UUID> allGuardianIds = retrievedChild.getAllGuardianIds();
        assertThat(allGuardianIds).hasSize(3);
        assertThat(allGuardianIds).contains(guardian1.getId(), guardian2.getId(), guardian3.getId());
        
        List<Guardian> allGuardians = retrievedChild.getAllGuardians();
        assertThat(allGuardians).hasSize(3);
        
        // Test primary guardian methods
        Guardian primaryGuardian = retrievedChild.getPrimaryGuardian();
        assertThat(primaryGuardian).isNotNull();
        assertThat(primaryGuardian.getId()).isEqualTo(guardian1.getId());
        assertThat(primaryGuardian.getRelationship()).isEqualTo("Mother");
        
        UUID primaryGuardianId = retrievedChild.getPrimaryGuardianId();
        assertThat(primaryGuardianId).isEqualTo(guardian1.getId());
    }

    @Test
    public void testChildWithNoGuardians() {
        // Create child without any guardians
        Child child = new Child();
        child.setFirstName("Bob");
        child.setLastName("Smith");
        child.setDob(Instant.parse("2019-03-20T00:00:00Z"));
        child.setGender("Male");
        child.setCreatedAt(Instant.now());
        child.setUpdatedAt(Instant.now());
        entityManager.persistAndFlush(child);

        entityManager.flush();
        entityManager.clear();

        // Retrieve the child and test methods with no guardians
        Child retrievedChild = entityManager.find(Child.class, child.getId());
        
        // Test all methods return empty/null safely
        assertThat(retrievedChild.getGuardianId()).isNull();
        assertThat(retrievedChild.getAllGuardianIds()).isEmpty();
        assertThat(retrievedChild.getAllGuardians()).isEmpty();
        assertThat(retrievedChild.getPrimaryGuardian()).isNull();
        assertThat(retrievedChild.getPrimaryGuardianId()).isNull();
    }
}
