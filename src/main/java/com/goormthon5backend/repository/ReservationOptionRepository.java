package com.goormthon5backend.repository;

import com.goormthon5backend.domain.entity.ReservationOption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationOptionRepository extends JpaRepository<ReservationOption, Long> {
}
