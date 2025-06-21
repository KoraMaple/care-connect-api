package com.careconnect.coreapi.childmgmt.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "enrollment")
public class Enrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "\"childId\"", nullable = false)
    private Child child;

    @Column(name = "\"startDate\"")
    private Instant startDate;

    @Column(name = "status", length = Integer.MAX_VALUE)
    private String status;

    @Column(name = "\"scheduleType\"", length = Integer.MAX_VALUE)
    private String scheduleType;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "\"createdAt\"", nullable = false)
    private Instant createdAt;

    @Column(name = "\"updatedAt\"", nullable = false)
    private Instant updatedAt;

}