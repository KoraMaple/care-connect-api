package com.careconnect.coreapi.childmgmt.jpa;

import com.careconnect.coreapi.user.User;
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
@Table(name = "guardians")
public class Guardian {
    @Id
    @Column(name = "id", nullable = false)
    @ColumnDefault("gen_random_uuid()")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull(message = "User is required")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotBlank(message = "Relationship is required")
    @Size(max = 50, message = "Relationship must not exceed 50 characters")
    @Column(name = "relationship", nullable = false, length = Integer.MAX_VALUE)
    private String relationship;

    @Size(max = 500, message = "Emergency contact must not exceed 500 characters")
    @Column(name = "emergency_contact", length = Integer.MAX_VALUE)
    private String emergencyContact;

    @ColumnDefault("false")
    @Column(name = "pickup_authorized", nullable = false)
    private Boolean pickupAuthorized = false;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

}