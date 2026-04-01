package com.goormthon5backend.domain.entity;

import com.goormthon5backend.domain.enums.ElementCategory;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "elements")
public class Element {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "element_id", nullable = false)
    private Long elementId;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private ElementCategory category;

    protected Element() {
    }
}
