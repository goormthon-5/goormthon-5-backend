package com.goormthon5backend.dto.accommodation;

import com.goormthon5backend.domain.entity.Accommodation;
import com.goormthon5backend.domain.entity.Address;
import com.fasterxml.jackson.annotation.JsonProperty;
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
        List<String> options
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
                .options(availableOptions)
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
        String name,
        Integer price
    ) {
    }

    public record AddressDto(
        @JsonProperty("address_group")
        String addressGroup,
        @JsonProperty("address_short")
        String addressShort,
        @JsonProperty("address_detail")
        String addressDetail,
        Double latitude,
        Double longitude
    ) {
        public static AddressDto from(Address address) {
            return new AddressDto(
                address.getAddressGroup(),
                address.getAddressShort(),
                address.getAddressDetail(),
                Double.valueOf(address.getLatitude()),
                Double.valueOf(address.getLongitude())
            );
        }
    }
}
