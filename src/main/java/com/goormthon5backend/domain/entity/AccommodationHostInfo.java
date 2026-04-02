package com.goormthon5backend.domain.entity;

import com.goormthon5backend.domain.enums.CleanlinessLevel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "accommodation_host_info")
@Getter
public class AccommodationHostInfo {

    @Id
    @Column(name = "accommodation_id", nullable = false)
    private Long accommodationId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "accommodation_id", nullable = false)
    private Accommodation accommodation;

    @Column(name = "personality", nullable = false)
    private String personality;

    @Column(name = "trait", nullable = false)
    private String trait;

    @Enumerated(EnumType.STRING)
    @Column(name = "cleanliness_level", nullable = false)
    private CleanlinessLevel cleanlinessLevel;

    @Column(name = "has_wifi", nullable = false)
    private Boolean hasWifi;

    protected AccommodationHostInfo() {
    }
}
