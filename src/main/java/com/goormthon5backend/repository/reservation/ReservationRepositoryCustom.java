package com.goormthon5backend.repository.reservation;

import java.util.List;

public interface ReservationRepositoryCustom {

    Long createReservation(Integer guestCount, Long userId);

    List<ReservationListRow> findReservationList(Long userId);
}
