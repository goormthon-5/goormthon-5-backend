package com.goormthon5backend.web.favorite;

import com.goormthon5backend.dto.favorite.FavoriteDto;
import com.goormthon5backend.service.favorite.FavoriteService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    @GetMapping
    public List<FavoriteDto.ListItemDto> list(@RequestParam Long userId) {
        return favoriteService.getFavoriteList(userId);
    }

    @GetMapping("/status")
    public FavoriteDto.StatusResponse status(
        @RequestParam Long userId,
        @RequestParam Long accommodationId
    ) {
        return favoriteService.getFavoriteStatus(userId, accommodationId);
    }

    @PostMapping
    public FavoriteDto.ActionResponse create(@RequestBody FavoriteDto.CreateRequest request) {
        return favoriteService.createFavorite(request);
    }

    @DeleteMapping
    public FavoriteDto.ActionResponse delete(
        @RequestParam Long userId,
        @RequestParam Long accommodationId
    ) {
        return favoriteService.deleteFavorite(userId, accommodationId);
    }
}
