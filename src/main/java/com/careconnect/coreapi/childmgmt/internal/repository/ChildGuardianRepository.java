package com.careconnect.coreapi.childmgmt.internal.repository;

import com.careconnect.coreapi.childmgmt.domain.ChildGuardian;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChildGuardianRepository extends JpaRepository<ChildGuardian, UUID> {

    List<ChildGuardian> findByChildId(UUID childId);

    List<ChildGuardian> findByGuardianId(UUID guardianId);

    Optional<ChildGuardian> findByChildIdAndGuardianId(UUID childId, UUID guardianId);

    @Query("SELECT cg FROM ChildGuardian cg WHERE cg.child.id = :childId AND cg.primaryGuardian = true")
    Optional<ChildGuardian> findPrimaryGuardianByChildId(@Param("childId") UUID childId);

    @Query("SELECT COUNT(cg) FROM ChildGuardian cg WHERE cg.child.id = :childId")
    Long countByChildId(@Param("childId") UUID childId);

    boolean existsByChildIdAndGuardianId(UUID childId, UUID guardianId);
}
