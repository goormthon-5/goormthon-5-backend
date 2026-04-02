package com.goormthon5backend.repository.accommodation;

import com.goormthon5backend.domain.entity.AccommodationImage;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AccommodationImageRepository extends JpaRepository<AccommodationImage, Long> {

    @Query("""
        SELECT ai.image.imageUrl
        FROM AccommodationImage ai
        WHERE ai.accommodation.accommodationId = :accommodationId
        ORDER BY ai.imageId ASC
        """)
    List<String> findImageUrlsByAccommodationId(@Param("accommodationId") Long accommodationId);
}
