package com.careconnect.coreapi.childmgmt.internal.repository;

import com.careconnect.coreapi.childmgmt.domain.Child;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ChildRepository extends JpaRepository<Child, UUID> {

    @Query("SELECT c FROM Child c WHERE " +
           "(:name IS NULL OR LOWER(c.firstName) LIKE LOWER(CONCAT('%', :name, '%')) OR LOWER(c.lastName) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
           "(:gender IS NULL OR c.gender = :gender)")
    Page<Child> findByFilters(@Param("name") String name,
                             @Param("gender") String gender,
                             Pageable pageable);

    List<Child> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String firstName, String lastName);

    @Query("SELECT c FROM Child c JOIN c.guardian g WHERE g.id = :guardianId")
    List<Child> findByGuardianId(@Param("guardianId") UUID guardianId);

    @Query("SELECT c FROM Child c JOIN ChildGuardian cg ON c.id = cg.child.id WHERE cg.guardian.id = :guardianId")
    List<Child> findByAssociatedGuardianId(@Param("guardianId") UUID guardianId);
}
