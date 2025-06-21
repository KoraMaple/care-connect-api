package com.careconnect.coreapi.facility.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DialectOverride;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "addresses")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @ColumnDefault("gen_random_uuid()")
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "street_line_1", nullable = false, length = Integer.MAX_VALUE)
    private String streetLine1;

    @Column(name = "street_line_2", length = Integer.MAX_VALUE)
    private String streetLine2;

    @Column(name = "city", nullable = false, length = Integer.MAX_VALUE)
    private String city;

    @Column(name = "province", nullable = false, length = Integer.MAX_VALUE)
    private String province;

    @Column(name = "postal_code", nullable = false, length = 5)
    private String postalCode;

    @Column(name = "country", nullable = false, length = Integer.MAX_VALUE)
    private String country;

    @OneToOne(mappedBy = "address")
    private Facility facility;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
}
