package com.goormthon5backend.repository;

import com.goormthon5backend.domain.entity.QInventory;
import com.goormthon5backend.domain.entity.QReservation;
import com.goormthon5backend.domain.entity.QReservationInventory;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReservationRepositoryImpl implements ReservationRepositoryCustom {

    private static final QReservation reservation = QReservation.reservation;
    private static final QReservationInventory reservationInventory = QReservationInventory.reservationInventory;
    private static final QInventory inventory = QInventory.inventory;

    private final EntityManager entityManager;
    private final JPAQueryFactory queryFactory;

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

    @Override
    public List<ReservationListRow> findReservationList(Long userId) {
        return queryFactory
            .select(
                reservation.reservatonId,
                inventory.accommodation.accommodationId.min(),
                reservation.user.userId,
                reservation.guestCount,
                inventory.stayDate.min(),
                inventory.stayDate.max()
            )
            .from(reservation)
            .leftJoin(reservationInventory).on(reservationInventory.reservation.eq(reservation))
            .leftJoin(reservationInventory.inventory, inventory)
            .where(userIdEq(userId))
            .groupBy(
                reservation.reservatonId,
                reservation.user.userId,
                reservation.guestCount
            )
            .orderBy(reservation.reservatonId.desc())
            .fetch()
            .stream()
            .map(tuple -> new ReservationListRow(
                tuple.get(reservation.reservatonId),
                tuple.get(inventory.accommodation.accommodationId.min()),
                tuple.get(reservation.user.userId),
                tuple.get(reservation.guestCount),
                toLocalDate(tuple.get(inventory.stayDate.min())),
                toLocalDate(tuple.get(inventory.stayDate.max()))
            ))
            .toList();
    }

    private BooleanExpression userIdEq(Long userId) {
        return userId == null ? null : reservation.user.userId.eq(userId);
    }

    private LocalDate toLocalDate(LocalDateTime value) {
        if (value == null) {
            return null;
        }
        return value.toLocalDate();
    }
}
