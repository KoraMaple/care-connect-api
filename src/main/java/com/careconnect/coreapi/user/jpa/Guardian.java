package com.careconnect.coreapi.user.jpa;

import jakarta.persistence.*;
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
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "\"userId\"", nullable = false)
    private User user;

    @Column(name = "relationship", nullable = false, length = Integer.MAX_VALUE)
    private String relationship;

    @Column(name = "\"emergencyContact\"", length = Integer.MAX_VALUE)
    private String emergencyContact;

    @ColumnDefault("false")
    @Column(name = "\"pickupAuthorized\"", nullable = false)
    private Boolean pickupAuthorized = false;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "\"createdAt\"", nullable = false)
    private Instant createdAt;

    @Column(name = "\"updatedAt\"", nullable = false)
    private Instant updatedAt;

}