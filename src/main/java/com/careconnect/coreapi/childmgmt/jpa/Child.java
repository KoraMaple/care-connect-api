package com.careconnect.coreapi.childmgmt.jpa;

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
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "children")
public class Child {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @ColumnDefault("gen_random_uuid()")
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @NotBlank(message = "First name is required")
    @Size(max = 100, message = "First name must not exceed 100 characters")
    @Column(name = "first_name", nullable = false, length = Integer.MAX_VALUE)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 100, message = "Last name must not exceed 100 characters")
    @Column(name = "last_name", nullable = false, length = Integer.MAX_VALUE)
    private String lastName;

    @NotNull(message = "Date of birth is required")
    @Column(name = "dob")
    private Instant dob;

    @Size(max = 10, message = "Gender must not exceed 10 characters")
    @Column(name = "gender", length = Integer.MAX_VALUE)
    private String gender;

    @Size(max = 1000, message = "Special needs must not exceed 1000 characters")
    @Column(name = "special_needs", length = Integer.MAX_VALUE)
    private String specialNeeds;

    @Size(max = 500, message = "Emergency contact must not exceed 500 characters")
    @Column(name = "emergency_contact", length = Integer.MAX_VALUE)
    private String emergencyContact;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "guardian_id")
    private Guardian guardian;

}