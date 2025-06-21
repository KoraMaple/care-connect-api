package com.careconnect.coreapi.childmgmt.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "enrollment")
public class Enrollment {
    @Id
    @Column(name = "id", nullable = false, length = Integer.MAX_VALUE)
    private String id;

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