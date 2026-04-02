package com.goormthon5backend.domain.entity;

import lombok.Getter;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable

@Getter
public class Address {

    @Column(name = "address_group", nullable = false)
    private String addressGroup;

    @Column(name = "address_short", nullable = false)
    private String addressShort;

    @Column(name = "address_detail", nullable = false)
    private String addressDetail;

    @Column(name = "postal_code", nullable = false)
    private String postalCode;

    @Column(name = "latitude", nullable = false)
    private Float latitude;

    @Column(name = "longitude", nullable = false)
    private Float longitude;

    protected Address() {
    }
}
