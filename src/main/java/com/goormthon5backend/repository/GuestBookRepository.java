package com.goormthon5backend.repository;

import com.goormthon5backend.domain.entity.GuestBook;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GuestBookRepository extends JpaRepository<GuestBook, Long> {

    @Query("""
        SELECT g.accommodation.accommodationId, AVG(g.rating), COUNT(g)
        FROM GuestBook g
        WHERE g.accommodation.accommodationId IN :accommodationIds
        GROUP BY g.accommodation.accommodationId
        """)
    List<Object[]> findRatingSummaryByAccommodationIds(@Param("accommodationIds") List<Long> accommodationIds);
}
