package com.careconnect.coreapi.billing.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class BillingTest {

    private Billing billing;

    @BeforeEach
    void setUp() {
        billing = new Billing();
    }

    @Test
    void setAndGetId_ShouldWorkCorrectly() {
        // Given
        UUID id = UUID.randomUUID();

        // When
        billing.setId(id);

        // Then
        assertThat(billing.getId()).isEqualTo(id);
    }

    @Test
    void setAndGetGuardianId_ShouldWorkCorrectly() {
        // Given
        UUID guardianId = UUID.randomUUID();

        // When
        billing.setGuardianId(guardianId);

        // Then
        assertThat(billing.getGuardianId()).isEqualTo(guardianId);
    }

    @Test
    void setAndGetAmount_ShouldWorkCorrectly() {
        // Given
        BigDecimal amount = new BigDecimal("100.50");

        // When
        billing.setAmount(amount);

        // Then
        assertThat(billing.getAmount()).isEqualTo(amount);
    }

    @Test
    void setAndGetDueDate_ShouldWorkCorrectly() {
        // Given
        Instant dueDate = Instant.now();

        // When
        billing.setDueDate(dueDate);

        // Then
        assertThat(billing.getDueDate()).isEqualTo(dueDate);
    }

    @Test
    void setAndGetCreatedAt_ShouldWorkCorrectly() {
        // Given
        Instant createdAt = Instant.now();

        // When
        billing.setCreatedAt(createdAt);

        // Then
        assertThat(billing.getCreatedAt()).isEqualTo(createdAt);
    }

    @Test
    void newBilling_ShouldHaveNullValues() {
        // Given
        Billing newBilling = new Billing();

        // Then
        assertThat(newBilling.getId()).isNull();
        assertThat(newBilling.getGuardianId()).isNull();
        assertThat(newBilling.getAmount()).isNull();
        assertThat(newBilling.getDueDate()).isNull();
        assertThat(newBilling.getCreatedAt()).isNull();
    }
}
