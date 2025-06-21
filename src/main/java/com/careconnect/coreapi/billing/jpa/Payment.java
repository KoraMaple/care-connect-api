package com.careconnect.coreapi.billing.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "\"billingId\"", nullable = false)
    private Billing billing;

    @Column(name = "amount", precision = 65, scale = 30)
    private BigDecimal amount;

    @Column(name = "\"paymentDate\"")
    private Instant paymentDate;

    @Column(name = "\"paymentMethod\"", length = Integer.MAX_VALUE)
    private String paymentMethod;

    @Column(name = "\"transactionId\"", length = Integer.MAX_VALUE)
    private String transactionId;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "\"createdAt\"", nullable = false)
    private Instant createdAt;

    @Column(name = "\"updatedAt\"", nullable = false)
    private Instant updatedAt;

}