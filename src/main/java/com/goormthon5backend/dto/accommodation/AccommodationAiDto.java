package com.goormthon5backend.dto.accommodation;

public final class AccommodationAiDto {

    private AccommodationAiDto() {
    }

    public record RewriteResponse(
        Long accommodationId,
        String name,
        String description
    ) {
    }
}
