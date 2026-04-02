package com.goormthon5backend.repository;

import java.time.LocalDate;

public record ReservationAvailabilityRow(
    Long inventoryId,
    LocalDate stayDate
) {
}
