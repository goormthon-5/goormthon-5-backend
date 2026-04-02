package com.goormthon5backend.repository;

import com.goormthon5backend.domain.entity.Element;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ElementRepository extends JpaRepository<Element, Long> {
}
