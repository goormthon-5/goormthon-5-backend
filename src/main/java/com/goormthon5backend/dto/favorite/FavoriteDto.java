package com.goormthon5backend.dto.favorite;

public final class FavoriteDto {

    private FavoriteDto() {
    }

    public record CreateRequest(
        Long userId,
        Long accommodationId
    ) {
    }

    public record ActionResponse(
        boolean success,
        String message,
        Long favoriteId
    ) {
    }

    public record StatusResponse(
        boolean isfavorited,
        Long favoriteId
    ) {
    }

    public record ListItemDto(
        Long favoriteId,
        Long accommodationId,
        String accommodationName,
        String addressGroup,
        String addressShort,
        String addressDetail,
        Integer cost,
        String imageUrl
    ) {
    }
}
