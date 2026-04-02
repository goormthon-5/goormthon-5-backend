package com.goormthon5backend.dto.accommodation;

import com.goormthon5backend.domain.entity.Accommodation;
import com.goormthon5backend.domain.entity.Address;
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
        Integer cost
    ) {
        public static ListItemDto from(Accommodation accommodation) {
            return ListItemDto.builder()
                .accommodationId(accommodation.getAccommodationId())
                .name(accommodation.getName())
                .description(accommodation.getDescription())
                .address(AddressDto.from(accommodation.getAddress()))
                .cost(accommodation.getCost())
                .build();
        }
    }

    @Builder
    public record DetailDto(
        Long accommodationId,
        String name,
        String description,
        AddressDto address,
        Integer cost
    ) {
        public static DetailDto from(Accommodation accommodation) {
            return DetailDto.builder()
                .accommodationId(accommodation.getAccommodationId())
                .name(accommodation.getName())
                .description(accommodation.getDescription())
                .address(AddressDto.from(accommodation.getAddress()))
                .cost(accommodation.getCost())
                .build();
        }
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
