package com.goormthon5backend.repository.reservation;

import java.time.LocalDate;

public record ReservationListRow(
    Long reservationId,
    Long accommodationId,
    Long userId,
    Integer guestCount,
    LocalDate startDate,
    LocalDate endDate
) {
}
