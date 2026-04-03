package com.goormthon5backend.repository.accommodation;

import com.goormthon5backend.domain.entity.AccommodationOption;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AccommodationOptionRepository extends JpaRepository<AccommodationOption, Long> {

    @Query("""
        SELECT ao.accommodation.accommodationId, ao.option.name
        FROM AccommodationOption ao
        WHERE ao.accommodation.accommodationId IN :accommodationIds
        """)
    List<Object[]> findOptionCategoriesByAccommodationIds(@Param("accommodationIds") List<Long> accommodationIds);

    @Query("""
        SELECT ao.option.optionId, ao.option.name, ao.cost
        FROM AccommodationOption ao
        WHERE ao.accommodation.accommodationId = :accommodationId
        ORDER BY ao.option.optionId ASC
        """)
    List<Object[]> findOptionDetailsByAccommodationId(@Param("accommodationId") Long accommodationId);
}
