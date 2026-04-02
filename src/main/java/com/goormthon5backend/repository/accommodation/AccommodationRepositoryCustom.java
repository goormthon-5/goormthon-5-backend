package com.goormthon5backend.repository.accommodation;

import com.goormthon5backend.domain.entity.Accommodation;
import java.util.List;

public interface AccommodationRepositoryCustom {

    List<Accommodation> findAllByAreaGroup(List<String> areaGroup);

    List<Accommodation> findAllByKeyword(String keyword);
}
