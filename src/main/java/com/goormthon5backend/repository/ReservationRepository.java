package com.goormthon5backend.repository;

import com.goormthon5backend.domain.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository
    extends JpaRepository<Reservation, Long>, ReservationRepositoryCustom {
}

