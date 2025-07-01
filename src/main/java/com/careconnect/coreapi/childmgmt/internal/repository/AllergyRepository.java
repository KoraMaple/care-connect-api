package com.careconnect.coreapi.childmgmt.internal;

import com.careconnect.coreapi.childmgmt.domain.Allergy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AllergyRepository extends JpaRepository<Allergy, UUID> {

    List<Allergy> findByNameContainingIgnoreCase(String name);

    Optional<Allergy> findByNameIgnoreCase(String name);

    List<Allergy> findBySeverity(String severity);
}
