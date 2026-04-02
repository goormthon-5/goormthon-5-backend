package com.goormthon5backend.service.accommodation;

import com.goormthon5backend.domain.entity.Accommodation;
import com.goormthon5backend.dto.accommodation.AccommodationDto;
import com.goormthon5backend.repository.AccommodationOptionRepository;
import com.goormthon5backend.repository.AccommodationImageRepository;
import com.goormthon5backend.repository.guest_book.GuestBookRepository;
import com.goormthon5backend.repository.accommodation.AccommodationRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AccommodationService {

    private final AccommodationRepository accommodationRepository;
    private final GuestBookRepository guestBookRepository;
    private final AccommodationOptionRepository accommodationOptionRepository;
    private final AccommodationImageRepository accommodationImageRepository;

    public List<AccommodationDto.ListItemDto> getAccommodationList(
        List<String> areaGroup,
        LocalDate startDate,
        LocalDate endDate
    ) {
        validateSearchFilters(startDate, endDate);
        List<Accommodation> accommodations = accommodationRepository.findAllByAreaGroup(
            areaGroup,
            startDate,
            endDate
        );
        return toListItemDtos(accommodations);
    }

    public List<AccommodationDto.ListItemDto> searchAccommodationList(
        String keyword,
        LocalDate startDate,
        LocalDate endDate
    ) {
        validateSearchFilters(startDate, endDate);
        List<Accommodation> accommodations = accommodationRepository.findAllByKeyword(
            keyword,
            startDate,
            endDate
        );
        return toListItemDtos(accommodations);
    }

    public AccommodationDto.DetailDto getAccommodationDetail(Long accommodationId) {
        Accommodation accommodation = accommodationRepository.findById(accommodationId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "숙소를 찾을 수 없습니다."));

        List<Object[]> ratingSummaryRows = guestBookRepository.findRatingSummaryByAccommodationId(accommodationId);
        Object[] ratingSummary = ratingSummaryRows.isEmpty() ? null : ratingSummaryRows.get(0);
        Double averageRating = 0.0;
        Long guestBookCount = 0L;
        if (ratingSummary != null) {
            if (ratingSummary[0] != null) {
                averageRating = ((Number) ratingSummary[0]).doubleValue();
            }
            if (ratingSummary[1] != null) {
                guestBookCount = ((Number) ratingSummary[1]).longValue();
            }
        }

        List<AccommodationDto.OptionDto> options = accommodationOptionRepository
            .findOptionDetailsByAccommodationId(accommodationId)
            .stream()
            .map(row -> new AccommodationDto.OptionDto(
                ((Number) row[0]).longValue(),
                String.valueOf(row[1]),
                row[2] != null ? ((Number) row[2]).intValue() : null
            ))
            .toList();

        String imageUrl = accommodationImageRepository.findImageUrlsByAccommodationId(accommodationId)
            .stream()
            .findFirst()
            .orElse(null);

        return AccommodationDto.DetailDto.from(
            accommodation,
            imageUrl,
            averageRating,
            guestBookCount,
            options
        );
    }

    private List<AccommodationDto.ListItemDto> toListItemDtos(List<Accommodation> accommodations) {
        if (accommodations.isEmpty()) {
            return List.of();
        }

        List<Long> accommodationIds = accommodations.stream()
            .map(Accommodation::getAccommodationId)
            .toList();

        Map<Long, Double> averageRatingByAccommodationId = new HashMap<>();
        Map<Long, Long> guestBookCountByAccommodationId = new HashMap<>();
        guestBookRepository.findRatingSummaryByAccommodationIds(accommodationIds).forEach(row -> {
            Long accommodationId = (Long) row[0];
            Double averageRating = row[1] != null ? ((Number) row[1]).doubleValue() : 0.0;
            Long guestBookCount = ((Number) row[2]).longValue();
            averageRatingByAccommodationId.put(accommodationId, averageRating);
            guestBookCountByAccommodationId.put(accommodationId, guestBookCount);
        });

        Map<Long, List<String>> availableOptionsByAccommodationId = new HashMap<>();
        accommodationOptionRepository.findOptionCategoriesByAccommodationIds(accommodationIds).forEach(row -> {
            Long accommodationId = (Long) row[0];
            String optionName = String.valueOf(row[1]);
            availableOptionsByAccommodationId
                .computeIfAbsent(accommodationId, ignored -> new ArrayList<>())
                .add(optionName);
        });

        return accommodations.stream()
            .map(accommodation -> {
                Long accommodationId = accommodation.getAccommodationId();
                return AccommodationDto.ListItemDto.from(
                    accommodation,
                    averageRatingByAccommodationId.getOrDefault(accommodationId, 0.0),
                    guestBookCountByAccommodationId.getOrDefault(accommodationId, 0L),
                    availableOptionsByAccommodationId.getOrDefault(accommodationId, Collections.emptyList())
                );
            })
            .toList();
    }

    private void validateSearchFilters(LocalDate startDate, LocalDate endDate) {
        boolean hasStartDate = startDate != null;
        boolean hasEndDate = endDate != null;
        if (hasStartDate != hasEndDate) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "startDate와 endDate는 함께 전달해야 합니다.");
        }
        if (hasStartDate && endDate.isBefore(startDate)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "endDate는 startDate보다 같거나 이후여야 합니다.");
        }
    }
}
