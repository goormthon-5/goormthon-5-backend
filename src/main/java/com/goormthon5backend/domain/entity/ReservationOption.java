package com.goormthon5backend.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "reservation_options")
public class ReservationOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "accommodation_option_id", nullable = false)
    private Long accommodationOptionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accommodation_option_id", insertable = false, updatable = false)
    private AccommodationOption accommodationOption;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservaton_id", nullable = false)
    private Reservation reservation;

    protected ReservationOption() {
    }
}
