package com.careconnect.coreapi.childmgmt.jpa;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "allergies")
public class Allergy {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @ColumnDefault("gen_random_uuid()")
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @NotBlank(message = "Allergy name is required")
    @Size(max = 100, message = "Allergy name must not exceed 100 characters")
    @Column(name = "name", nullable = false, length = Integer.MAX_VALUE)
    private String name;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @Size(max = 20, message = "Severity must not exceed 20 characters")
    @Column(name = "severity", length = Integer.MAX_VALUE)
    private String severity;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

}