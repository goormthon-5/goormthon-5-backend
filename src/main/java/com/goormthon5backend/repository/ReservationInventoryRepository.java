package com.goormthon5backend.repository;

import com.goormthon5backend.domain.entity.ReservationInventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationInventoryRepository extends JpaRepository<ReservationInventory, Long> {
}
