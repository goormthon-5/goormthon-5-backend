package com.goormthon5backend.repository.guest_book;

import com.goormthon5backend.dto.guest_book.GuestBookDto;
import java.util.List;

public interface GuestBookRepositoryCustom {

    List<Object[]> findRatingSummaryByAccommodationIds(List<Long> accommodationIds);

    List<Object[]> findRatingSummaryByAccommodationId(Long accommodationId);

    List<GuestBookDto.ListItemDto> findListByAccommodationId(Long accommodationId);
}
