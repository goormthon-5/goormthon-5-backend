package com.goormthon5backend.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Address {

    @Column(name = "main_address", nullable = false)
    private String mainAddress;

    @Column(name = "detail_address", nullable = false)
    private String detailAddress;

    @Column(name = "postal_code", nullable = false)
    private String postalCode;

    @Column(name = "latitude", nullable = false)
    private Float latitude;

    @Column(name = "longitude", nullable = false)
    private Float longitude;

    protected Address() {
    }
}
