package com.goormthon5backend.repository.guest_book;

import java.util.List;

public interface GuestBookRepositoryCustom {

    List<Object[]> findRatingSummaryByAccommodationIds(List<Long> accommodationIds);

    List<Object[]> findRatingSummaryByAccommodationId(Long accommodationId);
}
