package com.careconnect.coreapi.billing.jpa;

import com.careconnect.coreapi.user.jpa.Guardian;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "billing")
public class Billing {
    @Id
    @Column(name = "id", nullable = false, length = Integer.MAX_VALUE)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "\"guardianId\"", nullable = false)
    private Guardian guardian;

    @Column(name = "amount", precision = 65, scale = 30)
    private BigDecimal amount;

    @Column(name = "\"dueDate\"")
    private Instant dueDate;

    @Column(name = "status", length = Integer.MAX_VALUE)
    private String status;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "\"createdAt\"", nullable = false)
    private Instant createdAt;

    @Column(name = "\"updatedAt\"", nullable = false)
    private Instant updatedAt;

}