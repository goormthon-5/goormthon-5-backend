package com.goormthon5backend.domain.entity;

import com.goormthon5backend.domain.enums.OptionCategory;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "options")
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "option_id", nullable = false)
    private Long optionId;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private OptionCategory category;

    protected Option() {
    }
}
