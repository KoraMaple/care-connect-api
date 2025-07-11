package com.careconnect.coreapi.childmgmt.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "children")
public class Child {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @NotBlank(message = "First name is required")
    @Size(max = 100, message = "First name must not exceed 100 characters")
    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 100, message = "Last name must not exceed 100 characters")
    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @NotNull(message = "Date of birth is required")
    @Column(name = "dob")
    private Instant dob;

    @Size(max = 10, message = "Gender must not exceed 10 characters")
    @Column(name = "gender", length = 10)
    private String gender;

    @Size(max = 1000, message = "Special needs must not exceed 1000 characters")
    @Column(name = "special_needs", length = 1000)
    private String specialNeeds;

    @Size(max = 500, message = "Emergency contact must not exceed 500 characters")
    @Column(name = "emergency_contact", length = 500)
    private String emergencyContact;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    // Primary guardian (legacy/direct relationship)
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "guardian_id")
    @JsonIgnore
    private Guardian guardian;
    
    // All guardians through the join table
    @OneToMany(mappedBy = "child", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<ChildGuardian> childGuardians = new ArrayList<>();
    
    // Return all guardian IDs
    public List<UUID> getAllGuardianIds() {
        if (childGuardians == null || childGuardians.isEmpty()) {
            return new ArrayList<>();
        }
        return childGuardians.stream()
                .map(cg -> cg.getGuardian().getId())
                .toList();
    }
    
    // Return primary guardian (from child_guardians table)
    @JsonIgnore
    public Guardian getPrimaryGuardian() {
        if (childGuardians == null || childGuardians.isEmpty()) {
            return null;
        }
        return childGuardians.stream()
                .filter(cg -> Boolean.TRUE.equals(cg.getPrimaryGuardian()))
                .map(ChildGuardian::getGuardian)
                .findFirst()
                .orElse(null);
    }
    
    // Return primary guardian ID (from child_guardians table)
    public UUID getPrimaryGuardianId() {
        Guardian primaryGuardian = getPrimaryGuardian();
        return primaryGuardian != null ? primaryGuardian.getId() : null;
    }

}