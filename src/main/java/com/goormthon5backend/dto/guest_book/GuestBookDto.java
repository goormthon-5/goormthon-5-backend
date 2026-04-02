package com.goormthon5backend.dto.guest_book;

import com.goormthon5backend.domain.enums.GuestBookType;
import java.time.LocalDateTime;
import lombok.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public final class GuestBookDto {

    private GuestBookDto() {
    }

    @Builder
    public record ListItemDto(
        Long guestBookId,
        Long userId,
        String userName,
        String content,
        String type,
        Integer rating,
        String imageUrl,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
    ) {
        public static ListItemDto of(
            Long guestBookId,
            Long userId,
            String userName,
            String content,
            String type,
            Integer rating,
            String imageUrl,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
        ) {
            return ListItemDto.builder()
                .guestBookId(guestBookId)
                .userId(userId)
                .userName(userName)
                .content(content)
                .type(type)
                .rating(rating)
                .imageUrl(imageUrl)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
        }
    }

    public record CreateRequest(
        Long userId,
        String content,
        String type,
        Integer rating,
        String imageUrl
    ) {
        public GuestBookType validateAndGetType() {
            if (userId == null || type == null || type.isBlank()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "필수 값이 누락되었습니다.");
            }
            if (content == null || content.isBlank()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "content는 필수입니다.");
            }
            if (rating != null && (rating < 1 || rating > 5)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "rating은 1~5 범위여야 합니다.");
            }

            GuestBookType guestBookType;
            try {
                guestBookType = GuestBookType.valueOf(type.trim().toUpperCase());
            } catch (IllegalArgumentException ex) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "type은 TEXT 또는 IMAGE만 가능합니다.");
            }

            if (guestBookType == GuestBookType.IMAGE && (imageUrl == null || imageUrl.isBlank())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "IMAGE 타입은 imageUrl이 필수입니다.");
            }
            return guestBookType;
        }
    }

    public record CreateResponse(
        boolean success,
        String message,
        Long guestBookId
    ) {
    }
}
