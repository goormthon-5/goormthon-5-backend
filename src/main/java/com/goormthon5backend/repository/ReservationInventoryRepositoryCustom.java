package com.goormthon5backend.repository;

import java.time.LocalDate;
import java.util.List;

public interface ReservationInventoryRepositoryCustom {

    List<ReservationAvailabilityRow> findReservedInventories(
        Long accommodationId,
        LocalDate startDate,
        LocalDate endDate
    );

    void createReservationInventories(Long accommodationId, Long reservationId, List<LocalDate> stayDates);
}
