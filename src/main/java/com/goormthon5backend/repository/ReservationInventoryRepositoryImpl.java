package com.goormthon5backend.repository;

import com.goormthon5backend.domain.entity.QInventory;
import com.goormthon5backend.domain.entity.QReservationInventory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReservationInventoryRepositoryImpl implements ReservationInventoryRepositoryCustom {

    private static final QInventory inventory = QInventory.inventory;
    private static final QReservationInventory reservationInventory = QReservationInventory.reservationInventory;

    private final JPAQueryFactory queryFactory;
    private final EntityManager entityManager;

    @Override
    public List<ReservationAvailabilityRow> findReservedInventories(
        Long accommodationId,
        LocalDate startDate,
        LocalDate endDate
    ) {
        return queryFactory
            .select(inventory.inventoryId, inventory.stayDate)
            .from(inventory)
            .leftJoin(reservationInventory)
            .on(reservationInventory.inventoryId.eq(inventory.inventoryId))
            .where(
                inventory.accommodation.accommodationId.eq(accommodationId),
                inventory.stayDate.goe(startDate.atStartOfDay()),
                inventory.stayDate.lt(endDate.plusDays(1).atStartOfDay()),
                reservationInventory.inventoryId.isNotNull()
            )
            .orderBy(inventory.stayDate.asc(), inventory.inventoryId.asc())
            .fetch()
            .stream()
            .map(tuple -> new ReservationAvailabilityRow(
                tuple.get(inventory.inventoryId),
                tuple.get(inventory.stayDate).toLocalDate()
            ))
            .toList();
    }

    @Override
    public void createReservationInventories(Long accommodationId, Long reservationId, List<LocalDate> stayDates) {
        for (LocalDate stayDate : stayDates) {
            entityManager.createNativeQuery(
                """
                    INSERT INTO inventorys (accommodation_id, stay_date)
                    VALUES (:accommodationId, :stayDate)
                    """
            )
                .setParameter("accommodationId", accommodationId)
                .setParameter("stayDate", stayDate.atStartOfDay())
                .executeUpdate();

            Long inventoryId = ((Number) entityManager.createNativeQuery("SELECT LAST_INSERT_ID()").getSingleResult()).longValue();
            entityManager.createNativeQuery(
                """
                    INSERT INTO reservaton_inventory (inventory_id, reservaton_id)
                    VALUES (:inventoryId, :reservationId)
                    """
            )
                .setParameter("inventoryId", inventoryId)
                .setParameter("reservationId", reservationId)
                .executeUpdate();
        }
    }

}
