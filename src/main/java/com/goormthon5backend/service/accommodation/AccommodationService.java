package com.goormthon5backend.service.accommodation;

import com.goormthon5backend.domain.entity.Accommodation;
import com.goormthon5backend.dto.accommodation.AccommodationDto;
import com.goormthon5backend.repository.accommodation.AccommodationRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AccommodationService {

    private final AccommodationRepository accommodationRepository;

    public List<AccommodationDto.ListItemDto> getAccommodationList(List<String> areaGroup) {
        return accommodationRepository.findAllByAreaGroup(areaGroup)
            .stream()
            .map(AccommodationDto.ListItemDto::from)
            .toList();
    }

    public List<AccommodationDto.ListItemDto> searchAccommodationList(String keyword) {
        return accommodationRepository.findAllByKeyword(keyword)
            .stream()
            .map(AccommodationDto.ListItemDto::from)
            .toList();
    }

    public AccommodationDto.DetailDto getAccommodationDetail(Long accommodationId) {
        Accommodation accommodation = accommodationRepository.findById(accommodationId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "숙소를 찾을 수 없습니다."));

        return AccommodationDto.DetailDto.from(accommodation);
    }
}
