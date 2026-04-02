package com.goormthon5backend.repository.accommodation;

import com.goormthon5backend.domain.entity.Accommodation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccommodationRepository extends JpaRepository<Accommodation, Long>, AccommodationRepositoryCustom {
}
