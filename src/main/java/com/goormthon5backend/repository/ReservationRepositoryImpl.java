package com.goormthon5backend.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReservationRepositoryImpl implements ReservationRepositoryCustom {

    private final EntityManager entityManager;

    @Override
    public Long createReservation(Integer guestCount, Long userId) {
        entityManager.createNativeQuery(
            """
                INSERT INTO reservatons (guest_count, created_at, user_id)
                VALUES (:guestCount, NOW(), :userId)
                """
        )
            .setParameter("guestCount", guestCount)
            .setParameter("userId", userId)
            .executeUpdate();

        return ((Number) entityManager.createNativeQuery("SELECT LAST_INSERT_ID()").getSingleResult()).longValue();
    }
}
