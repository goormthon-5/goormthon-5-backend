package com.goormthon5backend.service.reservation;

import com.goormthon5backend.dto.reservation.ReservationDto;
import com.goormthon5backend.repository.ReservationAvailabilityRow;
import com.goormthon5backend.repository.ReservationInventoryRepository;
import com.goormthon5backend.repository.ReservationListRow;
import com.goormthon5backend.repository.ReservationRepository;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationInventoryRepository reservationInventoryRepository;

    public List<ReservationDto.ListItemDto> getReservationList(Long userId) {
        return reservationRepository.findReservationList(userId)
            .stream()
            .map(this::toListItemDto)
            .toList();
    }

    @Transactional
    public ReservationDto.CreateResponse createReservation(ReservationDto.CreateRequest request) {
        validateRequest(request);

        List<LocalDate> stayDates = request.startDate().datesUntil(request.endDate().plusDays(1)).toList();

        List<ReservationAvailabilityRow> reservedInventories = reservationInventoryRepository.findReservedInventories(
            request.accommodationId(),
            request.startDate(),
            request.endDate()
        );

        boolean hasReservation = !reservedInventories.isEmpty();
        if (hasReservation) {
            return new ReservationDto.CreateResponse(true, "해당 날짜에 예약이 있습니다.", null);
        }

        Long reservationId = reservationRepository.createReservation(request.guestCount(), request.userId());
        reservationInventoryRepository.createReservationInventories(
            request.accommodationId(),
            reservationId,
            stayDates
        );

        return new ReservationDto.CreateResponse(false, "예약이 완료되었습니다.", reservationId);
    }

    private void validateRequest(ReservationDto.CreateRequest request) {
        if (request == null
            || request.accommodationId() == null
            || request.userId() == null
            || request.startDate() == null
            || request.endDate() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "필수 값이 누락되었습니다.");
        }

        long nights = ChronoUnit.DAYS.between(request.startDate(), request.endDate());
        if (nights < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "endDate는 startDate보다 같거나 이후여야 합니다.");
        }
    }

    private ReservationDto.ListItemDto toListItemDto(ReservationListRow row) {
        return new ReservationDto.ListItemDto(
            row.reservationId(),
            row.accommodationId(),
            row.userId(),
            row.guestCount(),
            row.startDate(),
            row.endDate()
        );
    }
}
