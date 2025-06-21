package com.careconnect.coreapi.childmgmt.jpa;

import com.careconnect.coreapi.user.jpa.Guardian;
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
@Table(name = "children")
public class Child {
    @Id
    @Column(name = "id", nullable = false, length = Integer.MAX_VALUE)
    private String id;

    @Column(name = "\"firstName\"", nullable = false, length = Integer.MAX_VALUE)
    private String firstName;

    @Column(name = "\"lastName\"", nullable = false, length = Integer.MAX_VALUE)
    private String lastName;

    @Column(name = "dob")
    private Instant dob;

    @Column(name = "gender", length = Integer.MAX_VALUE)
    private String gender;

    @Column(name = "\"specialNeeds\"", length = Integer.MAX_VALUE)
    private String specialNeeds;

    @Column(name = "\"emergencyContact\"", length = Integer.MAX_VALUE)
    private String emergencyContact;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "\"createdAt\"", nullable = false)
    private Instant createdAt;

    @Column(name = "\"updatedAt\"", nullable = false)
    private Instant updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "\"guardianId\"")
    private Guardian guardian;

}