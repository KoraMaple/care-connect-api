package com.careconnect.coreapi.childmgmt.internal;

import com.careconnect.coreapi.childmgmt.domain.ChildAllergy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ChildAllergyRepository extends JpaRepository<ChildAllergy, UUID> {

    List<ChildAllergy> findByChildId(UUID childId);

    List<ChildAllergy> findByAllergyId(UUID allergyId);

    @Query("SELECT ca FROM ChildAllergy ca WHERE ca.child.id = :childId AND ca.allergy.severity = :severity")
    List<ChildAllergy> findByChildIdAndSeverity(@Param("childId") UUID childId, @Param("severity") String severity);

    boolean existsByChildIdAndAllergyId(UUID childId, UUID allergyId);

    void deleteByChildIdAndAllergyId(UUID childId, UUID allergyId);
}
