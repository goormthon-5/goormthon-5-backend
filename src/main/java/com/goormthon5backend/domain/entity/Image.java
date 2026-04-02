package com.goormthon5backend.domain.entity;

import lombok.Getter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "images")

@Getter
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id", nullable = false)
    private Long imageId;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    protected Image() {
    }
}
