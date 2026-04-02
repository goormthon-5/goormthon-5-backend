package com.goormthon5backend.domain.entity;

import lombok.Getter;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "accommodations")

@Getter
public class Accommodation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "accommodation_id", nullable = false)
    private Long accommodationId;

    @Column(name = "name", nullable = false)
    private String name;

    @Lob
    @Column(name = "description", nullable = false)
    private String description;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "mainAddress", column = @Column(name = "main_address", nullable = false)),
        @AttributeOverride(name = "detailAddress", column = @Column(name = "detail_address", nullable = false)),
        @AttributeOverride(name = "postalCode", column = @Column(name = "postal_code", nullable = false)),
        @AttributeOverride(name = "latitude", column = @Column(name = "latitude", nullable = false)),
        @AttributeOverride(name = "longitude", column = @Column(name = "longitude", nullable = false))
    })
    private Address address;

    @Column(name = "cost", nullable = false)
    private Integer cost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    protected Accommodation() {
    }
}
