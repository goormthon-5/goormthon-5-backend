package com.goormthon5backend.dto.guest_book;

import java.time.LocalDateTime;
import lombok.Builder;

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
}
