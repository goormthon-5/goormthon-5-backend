package com.goormthon5backend.service.accommodation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccommodationAiAsyncService {

    private final AccommodationService accommodationService;

    @Async
    public void rewriteAccommodationAsync(Long accommodationId) {
        try {
            accommodationService.rewriteAccommodationByTextGuestBooks(accommodationId);
        } catch (Exception e) {
            log.warn("숙소 AI 리라이트 백그라운드 실행 실패. accommodationId={}", accommodationId, e);
        }
    }
}
