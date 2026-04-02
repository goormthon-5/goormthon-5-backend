package com.goormthon5backend.repository.favorite;

import com.goormthon5backend.domain.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite, Long>, FavoriteRepositoryCustom {
}
