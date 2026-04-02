package com.goormthon5backend.web.accommodation;

import com.goormthon5backend.dto.accommodation.AccommodationDto;
import com.goormthon5backend.service.accommodation.AccommodationService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/accommodations")
public class AccommodationController {

    private final AccommodationService accommodationService;

    @GetMapping
    public List<AccommodationDto.ListItemDto> list(
        @RequestParam(required = false) List<String> areaGroup
    ) {
        return accommodationService.getAccommodationList(areaGroup);
    }

    @GetMapping("/search")
    public List<AccommodationDto.ListItemDto> search(
        @RequestParam String keyword
    ) {
        return accommodationService.searchAccommodationList(keyword);
    }

    @GetMapping("/{accommodationId}")
    public AccommodationDto.DetailDto detail(@PathVariable Long accommodationId) {
        return accommodationService.getAccommodationDetail(accommodationId);
    }
}
