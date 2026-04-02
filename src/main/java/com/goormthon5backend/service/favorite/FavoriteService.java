package com.goormthon5backend.service.favorite;

import com.goormthon5backend.dto.favorite.FavoriteDto;
import com.goormthon5backend.repository.user.UserRepository;
import com.goormthon5backend.repository.accommodation.AccommodationRepository;
import com.goormthon5backend.repository.favorite.FavoriteRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final AccommodationRepository accommodationRepository;

    @Transactional
    public FavoriteDto.ActionResponse createFavorite(FavoriteDto.CreateRequest request) {
        if (request == null || request.userId() == null || request.accommodationId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "필수 값이 누락되었습니다.");
        }

        validateUserAndAccommodation(request.userId(), request.accommodationId());

        if (favoriteRepository.existsFavorite(request.userId(), request.accommodationId())) {
            Long favoriteId = favoriteRepository.findFavoriteId(request.userId(), request.accommodationId());
            return new FavoriteDto.ActionResponse(false, "이미 즐겨찾기에 등록된 숙소입니다.", favoriteId);
        }

        Long favoriteId = favoriteRepository.createFavorite(request.userId(), request.accommodationId());
        return new FavoriteDto.ActionResponse(true, "즐겨찾기에 추가되었습니다.", favoriteId);
    }

    @Transactional
    public FavoriteDto.ActionResponse deleteFavorite(Long userId, Long accommodationId) {
        if (userId == null || accommodationId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "userId와 accommodationId는 필수입니다.");
        }

        validateUserAndAccommodation(userId, accommodationId);

        Long favoriteId = favoriteRepository.findFavoriteId(userId, accommodationId);
        if (favoriteId == null) {
            return new FavoriteDto.ActionResponse(false, "즐겨찾기 내역이 없습니다.", null);
        }

        favoriteRepository.deleteFavorite(userId, accommodationId);
        return new FavoriteDto.ActionResponse(true, "즐겨찾기가 해제되었습니다.", favoriteId);
    }

    public FavoriteDto.StatusResponse getFavoriteStatus(Long userId, Long accommodationId) {
        if (userId == null || accommodationId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "userId와 accommodationId는 필수입니다.");
        }

        validateUserAndAccommodation(userId, accommodationId);

        Long favoriteId = favoriteRepository.findFavoriteId(userId, accommodationId);
        return new FavoriteDto.StatusResponse(favoriteId != null, favoriteId);
    }

    public List<FavoriteDto.ListItemDto> getFavoriteList(Long userId) {
        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "userId는 필수입니다.");
        }
        if (!userRepository.existsById(userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다.");
        }

        return favoriteRepository.findFavoriteList(userId);
    }

    private void validateUserAndAccommodation(Long userId, Long accommodationId) {
        if (!userRepository.existsById(userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다.");
        }
        if (!accommodationRepository.existsById(accommodationId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "숙소를 찾을 수 없습니다.");
        }
    }
}
