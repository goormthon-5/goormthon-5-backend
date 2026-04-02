package com.goormthon5backend.service.accommodation;

import com.goormthon5backend.domain.entity.Accommodation;
import com.goormthon5backend.domain.entity.AccommodationHostInfo;
import com.goormthon5backend.dto.accommodation.AccommodationAiDto;
import com.goormthon5backend.dto.accommodation.AccommodationDto;
import com.goormthon5backend.repository.AccommodationHostInfoRepository;
import com.goormthon5backend.repository.AccommodationOptionRepository;
import com.goormthon5backend.repository.AccommodationImageRepository;
import com.goormthon5backend.repository.guest_book.GuestBookRepository;
import com.goormthon5backend.repository.accommodation.AccommodationRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AccommodationService {

    private final AccommodationRepository accommodationRepository;
    private final GuestBookRepository guestBookRepository;
    private final AccommodationOptionRepository accommodationOptionRepository;
    private final AccommodationImageRepository accommodationImageRepository;
    private final AccommodationHostInfoRepository accommodationHostInfoRepository;
    private final ChatClient chatClient;
    private final ObjectMapper objectMapper;

    public AccommodationService(
        AccommodationRepository accommodationRepository,
        GuestBookRepository guestBookRepository,
        AccommodationOptionRepository accommodationOptionRepository,
        AccommodationImageRepository accommodationImageRepository,
        AccommodationHostInfoRepository accommodationHostInfoRepository,
        ChatClient.Builder chatClientBuilder,
        ObjectMapper objectMapper
    ) {
        this.accommodationRepository = accommodationRepository;
        this.guestBookRepository = guestBookRepository;
        this.accommodationOptionRepository = accommodationOptionRepository;
        this.accommodationImageRepository = accommodationImageRepository;
        this.accommodationHostInfoRepository = accommodationHostInfoRepository;
        this.chatClient = chatClientBuilder.build();
        this.objectMapper = objectMapper;
    }

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
                averageRating = roundToOneDecimal(((Number) ratingSummary[0]).doubleValue());
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
        AccommodationHostInfo hostInfo = accommodationHostInfoRepository.findById(accommodationId).orElse(null);

        return AccommodationDto.DetailDto.from(
            accommodation,
            imageUrl,
            hostInfo != null ? hostInfo.getPersonality() : null,
            hostInfo != null ? hostInfo.getTrait() : null,
            hostInfo != null ? hostInfo.getCleanlinessLevel().name() : null,
            hostInfo != null ? hostInfo.getHasWifi() : null,
            averageRating,
            guestBookCount,
            options
        );
    }

    @Transactional
    public AccommodationAiDto.RewriteResponse rewriteAccommodationByTextGuestBooks(Long accommodationId) {
        Accommodation accommodation = accommodationRepository.findById(accommodationId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "숙소를 찾을 수 없습니다."));

        List<String> textGuestBooks = guestBookRepository.findTextContentsByAccommodationId(accommodationId);
        if (textGuestBooks.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "TEXT 타입 방명록이 없습니다.");
        }

        String reviews = String.join("\n- ", textGuestBooks);
        String originalName = accommodation.getName();
        String fixedRegion = extractRegion(originalName);
        String fixedSuffix = extractSuffix(originalName);
        String prompt = """
        아래는 특정 숙소의 TEXT 방명록 모음입니다.
        방명록 내용을 바탕으로 숙소의 이름(name)과 설명(description)을 새로 작성하세요.
        
        [목표]
        - 방명록에서 공통적으로 드러나는 분위기, 활동, 감정, 특징을 반영
        - 실제 방문 경험 기반으로 신뢰감 있는 소개 생성
        
        [이름(name) 생성 규칙 - 매우 중요]
        1) 반드시 "지역 + 키워드 + 할망/삼춘" 형태로 작성
        2) 지역과 마지막 호칭(할망/삼춘)은 기존 숙소명과 동일하게 유지 (절대 변경 금지)
        3) 키워드는 방명록에서 드러나는 대표 특징 1개를 자연스럽게 표현
           - 예: 다정한, 손맛 좋은, 바다 좋아하는, 이야기 많은, 정 많은
        4) 마지막은 반드시 기존과 같은 "할망" 또는 "삼춘"으로 끝낼 것
        5) 25자 이내
        6) 어색한 번역투 금지 (자연스러운 제주 감성)
        
        [설명(description) 생성 규칙 - 매우 짧게]
        1) 방명록 기반으로 실제 경험을 요약
        2) 숙소의 분위기, 활동(식사, 체험 등), 사람의 정을 포함
        3) 과장/허위 표현 금지
        4) 60자 이내 (짧은 소개 문장 1~2문장)
        5) 자연스럽고 따뜻한 톤
        
        [출력 형식 - 반드시 준수]
        - JSON만 출력 (설명, 마크다운, 코드블럭 절대 금지)
        - 키는 정확히 name, description

        [기존 숙소명]
        - %s

        [방명록]
        %s
        """.formatted(originalName, reviews);

        String aiContent = chatClient.prompt()
            .user(prompt)
            .call()
            .content();

        RewritePayload payload;
        try {
            payload = objectMapper.readValue(aiContent, RewritePayload.class);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "AI 응답 파싱에 실패했습니다.");
        }

        if (payload.name() == null || payload.name().isBlank()
            || payload.description() == null || payload.description().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "AI 응답 형식이 올바르지 않습니다.");
        }

        String rewrittenName = limitLength(
            enforceFixedNameParts(payload.name().trim(), fixedRegion, fixedSuffix, originalName),
            25
        );
        String rewrittenDescription = limitLength(payload.description().trim(), 60);
        accommodationRepository.updateNameAndDescription(accommodationId, rewrittenName, rewrittenDescription);

        return new AccommodationAiDto.RewriteResponse(
            accommodation.getAccommodationId(),
            rewrittenName,
            rewrittenDescription
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
            Double averageRating = row[1] != null ? roundToOneDecimal(((Number) row[1]).doubleValue()) : 0.0;
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

    private Double roundToOneDecimal(Double value) {
        return BigDecimal.valueOf(value)
            .setScale(1, RoundingMode.HALF_UP)
            .doubleValue();
    }

    private String limitLength(String value, int maxLength) {
        if (value == null) {
            return null;
        }
        return value.length() <= maxLength ? value : value.substring(0, maxLength);
    }

    private String extractRegion(String name) {
        if (name == null || name.isBlank()) {
            return "";
        }
        String[] parts = name.trim().split("\\s+");
        return parts.length > 0 ? parts[0] : "";
    }

    private String extractSuffix(String name) {
        if (name == null || name.isBlank()) {
            return "";
        }
        if (name.contains("할망")) {
            return "할망";
        }
        if (name.contains("삼춘")) {
            return "삼춘";
        }
        return "";
    }

    private String enforceFixedNameParts(String candidate, String region, String suffix, String fallbackName) {
        String core = candidate == null ? "" : candidate.trim();

        if (!region.isBlank() && core.startsWith(region)) {
            core = core.substring(region.length()).trim();
        }
        if (!suffix.isBlank() && core.endsWith(suffix)) {
            core = core.substring(0, core.length() - suffix.length()).trim();
        }

        if (core.isBlank()) {
            core = fallbackName == null ? "" : fallbackName.trim();
            if (!region.isBlank() && core.startsWith(region)) {
                core = core.substring(region.length()).trim();
            }
            if (!suffix.isBlank() && core.endsWith(suffix)) {
                core = core.substring(0, core.length() - suffix.length()).trim();
            }
        }

        if (region.isBlank() || suffix.isBlank()) {
            return candidate;
        }
        if (core.isBlank()) {
            return region + " " + suffix;
        }
        return region + " " + core + " " + suffix;
    }

    private record RewritePayload(
        String name,
        String description
    ) {
    }
}
