package com.goormthon5backend.web.accommodation;

import com.goormthon5backend.dto.accommodation.AccommodationAiDto;
import com.goormthon5backend.dto.accommodation.AccommodationDto;
import com.goormthon5backend.service.accommodation.AccommodationService;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
        @RequestParam(required = false) List<String> areaGroup,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        return accommodationService.getAccommodationList(areaGroup, startDate, endDate);
    }

    @GetMapping("/search")
    public List<AccommodationDto.ListItemDto> search(
        @RequestParam(required = false) String keyword,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        return accommodationService.searchAccommodationList(keyword, startDate, endDate);
    }

    @GetMapping("/{accommodationId}")
    public AccommodationDto.DetailDto detail(@PathVariable Long accommodationId) {
        return accommodationService.getAccommodationDetail(accommodationId);
    }

    @PostMapping("/{accommodationId}/ai-rewrite")
    public AccommodationAiDto.RewriteResponse aiRewrite(@PathVariable Long accommodationId) {
        return accommodationService.rewriteAccommodationByTextGuestBooks(accommodationId);
    }
}
