package com.goormthon5backend.service.reservation;

import com.goormthon5backend.domain.entity.AccommodationHostInfo;
import com.goormthon5backend.dto.accommodation.AccommodationDto;
import com.goormthon5backend.dto.reservation.ReservationDto;
import com.goormthon5backend.repository.accommodation.AccommodationHostInfoRepository;
import com.goormthon5backend.repository.reservation.ReservationAvailabilityRow;
import com.goormthon5backend.repository.reservation.ReservationInventoryRepository;
import com.goormthon5backend.repository.reservation.ReservationListRow;
import com.goormthon5backend.repository.reservation.ReservationRepository;
import com.goormthon5backend.repository.guest_book.GuestBookRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private final GuestBookRepository guestBookRepository;
    private final AccommodationHostInfoRepository accommodationHostInfoRepository;

    public List<ReservationDto.ListItemDto> getReservationList(Long userId) {
        List<ReservationListRow> reservationRows = reservationRepository.findReservationList(userId);
        if (reservationRows.isEmpty()) {
            return List.of();
        }

        List<Long> accommodationIds = reservationRows.stream()
            .map(ReservationListRow::accommodationId)
            .filter(id -> id != null)
            .distinct()
            .toList();

        Map<Long, Double> averageRatingByAccommodationId = new HashMap<>();
        Map<Long, Long> guestBookCountByAccommodationId = new HashMap<>();
        guestBookRepository.findRatingSummaryByAccommodationIds(accommodationIds).forEach(row -> {
            Long accommodationId = (Long) row[0];
            Double averageRating = row[1] != null ? roundToOneDecimal(((Number) row[1]).doubleValue()) : 0.0;
            Long guestBookCount = row[2] != null ? ((Number) row[2]).longValue() : 0L;
            averageRatingByAccommodationId.put(accommodationId, averageRating);
            guestBookCountByAccommodationId.put(accommodationId, guestBookCount);
        });

        Map<Long, AccommodationDto.AccommodationHostInfoDto> hostInfoByAccommodationId = new HashMap<>();
        accommodationHostInfoRepository.findAllById(accommodationIds).forEach(hostInfo ->
            hostInfoByAccommodationId.put(hostInfo.getAccommodationId(), toAccommodationHostInfoDto(hostInfo))
        );

        return reservationRows
            .stream()
            .map(row -> toListItemDto(
                row,
                hostInfoByAccommodationId.get(row.accommodationId()),
                averageRatingByAccommodationId.getOrDefault(row.accommodationId(), 0.0),
                guestBookCountByAccommodationId.getOrDefault(row.accommodationId(), 0L)
            ))
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

    private ReservationDto.ListItemDto toListItemDto(
        ReservationListRow row,
        AccommodationDto.AccommodationHostInfoDto accommodationHostInfo,
        Double averageRating,
        Long guestBookCount
    ) {
        return new ReservationDto.ListItemDto(
            row.reservationId(),
            row.accommodationId(),
            row.userId(),
            row.guestCount(),
            row.startDate(),
            row.endDate(),
            accommodationHostInfo,
            averageRating,
            guestBookCount
        );
    }

    private AccommodationDto.AccommodationHostInfoDto toAccommodationHostInfoDto(AccommodationHostInfo hostInfo) {
        if (hostInfo == null) {
            return null;
        }
        return new AccommodationDto.AccommodationHostInfoDto(
            hostInfo.getPersonality(),
            hostInfo.getTrait(),
            hostInfo.getCleanlinessLevel() != null ? hostInfo.getCleanlinessLevel().name() : null,
            hostInfo.getHasWifi()
        );
    }

    private Double roundToOneDecimal(Double value) {
        return BigDecimal.valueOf(value)
            .setScale(1, RoundingMode.HALF_UP)
            .doubleValue();
    }
}
