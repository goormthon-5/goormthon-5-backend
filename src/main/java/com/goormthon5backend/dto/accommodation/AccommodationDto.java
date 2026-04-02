package com.goormthon5backend.dto.accommodation;

import com.goormthon5backend.domain.entity.Accommodation;
import com.goormthon5backend.domain.entity.Address;
import java.util.List;
import lombok.Builder;

public final class AccommodationDto {

    private AccommodationDto() {
    }

    @Builder
    public record ListItemDto(
        Long accommodationId,
        String name,
        String description,
        AddressDto address,
        Integer cost,
        Double averageRating,
        Long guestBookCount,
        List<String> availableOptions
    ) {
        public static ListItemDto from(
            Accommodation accommodation,
            Double averageRating,
            Long guestBookCount,
            List<String> availableOptions
        ) {
            return ListItemDto.builder()
                .accommodationId(accommodation.getAccommodationId())
                .name(accommodation.getName())
                .description(accommodation.getDescription())
                .address(AddressDto.from(accommodation.getAddress()))
                .cost(accommodation.getCost())
                .averageRating(averageRating)
                .guestBookCount(guestBookCount)
                .availableOptions(availableOptions)
                .build();
        }
    }

    @Builder
    public record DetailDto(
        Long accommodationId,
        String name,
        String description,
        AddressDto address,
        Integer cost,
        String imageUrl,
        Double averageRating,
        Long guestBookCount,
        List<OptionDto> options
    ) {
        public static DetailDto from(
            Accommodation accommodation,
            String imageUrl,
            Double averageRating,
            Long guestBookCount,
            List<OptionDto> options
        ) {
            return DetailDto.builder()
                .accommodationId(accommodation.getAccommodationId())
                .name(accommodation.getName())
                .description(accommodation.getDescription())
                .address(AddressDto.from(accommodation.getAddress()))
                .cost(accommodation.getCost())
                .imageUrl(imageUrl)
                .averageRating(averageRating)
                .guestBookCount(guestBookCount)
                .options(options)
                .build();
        }
    }

    public record OptionDto(
        Long optionId,
        String category,
        Integer price
    ) {
    }

    public record AddressDto(
        String mainAddress,
        String detailAddress,
        String postalCode,
        Double latitude,
        Double longitude
    ) {
        public static AddressDto from(Address address) {
            return new AddressDto(
                address.getMainAddress(),
                address.getDetailAddress(),
                address.getPostalCode(),
                Double.valueOf(address.getLatitude()),
                Double.valueOf(address.getLongitude())
            );
        }
    }
}
