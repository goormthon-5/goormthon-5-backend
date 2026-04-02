package com.goormthon5backend.repository.favorite;

import com.goormthon5backend.dto.favorite.FavoriteDto;
import java.util.List;

public interface FavoriteRepositoryCustom {

    Long createFavorite(Long userId, Long accommodationId);

    void deleteFavorite(Long userId, Long accommodationId);

    boolean existsFavorite(Long userId, Long accommodationId);

    Long findFavoriteId(Long userId, Long accommodationId);

    List<FavoriteDto.ListItemDto> findFavoriteList(Long userId);
}
