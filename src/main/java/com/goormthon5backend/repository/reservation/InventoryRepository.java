package com.goormthon5backend.repository.reservation;

import com.goormthon5backend.domain.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
}
