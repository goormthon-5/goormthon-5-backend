package com.goormthon5backend.repository.accommodation;

import com.goormthon5backend.domain.entity.Accommodation;
import java.time.LocalDate;
import java.util.List;

public interface AccommodationRepositoryCustom {

    List<Accommodation> findAllByAreaGroup(
        List<String> areaGroup,
        LocalDate startDate,
        LocalDate endDate
    );

    List<Accommodation> findAllByKeyword(
        String keyword,
        LocalDate startDate,
        LocalDate endDate
    );

    void updateNameAndDescription(Long accommodationId, String name, String description);
}
