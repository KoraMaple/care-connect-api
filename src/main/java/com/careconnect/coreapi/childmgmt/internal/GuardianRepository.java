package com.careconnect.coreapi.childmgmt.repository;

import com.careconnect.coreapi.childmgmt.jpa.Guardian;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GuardianRepository extends JpaRepository<Guardian, UUID> {

    Optional<Guardian> findByUserId(UUID userId);

    @Query("SELECT g FROM Guardian g JOIN User u ON g.userId = u.id WHERE u.clerkUserId = :clerkUserId")
    Optional<Guardian> findByUserClerkUserId(@Param("clerkUserId") String clerkUserId);

    List<Guardian> findByRelationship(String relationship);

    List<Guardian> findByPickupAuthorized(Boolean pickupAuthorized);
}
