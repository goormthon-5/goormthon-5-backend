package com.goormthon5backend.dto.reservation;

import java.time.LocalDate;

public final class ReservationDto {

    private ReservationDto() {
    }

    public record CreateRequest(
        Long accommodationId,
        Long userId,
        Integer guestCount,
        LocalDate startDate,
        LocalDate endDate
    ) {
    }

    public record CreateResponse(
        boolean hasReservation,
        String message,
        Long reservationId
    ) {
    }

    public record ListItemDto(
        Long reservationId,
        Long accommodationId,
        Long userId,
        Integer guestCount,
        LocalDate startDate,
        LocalDate endDate,
        Double averageRating,
        Long guestBookCount
    ) {
    }
}
