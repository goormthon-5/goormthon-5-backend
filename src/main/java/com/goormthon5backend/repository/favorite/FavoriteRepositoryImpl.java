package com.goormthon5backend.repository.favorite;

import com.goormthon5backend.domain.entity.QAccommodation;
import com.goormthon5backend.domain.entity.QAccommodationImage;
import com.goormthon5backend.domain.entity.QFavorite;
import com.goormthon5backend.domain.entity.QImage;
import com.goormthon5backend.dto.favorite.FavoriteDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FavoriteRepositoryImpl implements FavoriteRepositoryCustom {

    private static final QFavorite favorite = QFavorite.favorite;
    private static final QAccommodation accommodation = QAccommodation.accommodation;
    private static final QAccommodationImage accommodationImage = QAccommodationImage.accommodationImage;
    private static final QImage image = QImage.image;

    private final EntityManager entityManager;
    private final JPAQueryFactory queryFactory;

    @Override
    public Long createFavorite(Long userId, Long accommodationId) {
        entityManager.createNativeQuery(
            """
                INSERT INTO favorites (accommodation_id, user_id)
                VALUES (:accommodationId, :userId)
                """
        )
            .setParameter("accommodationId", accommodationId)
            .setParameter("userId", userId)
            .executeUpdate();

        return ((Number) entityManager.createNativeQuery("SELECT LAST_INSERT_ID()").getSingleResult()).longValue();
    }

    @Override
    public void deleteFavorite(Long userId, Long accommodationId) {
        entityManager.createNativeQuery(
            """
                DELETE FROM favorites
                WHERE user_id = :userId
                  AND accommodation_id = :accommodationId
                """
        )
            .setParameter("userId", userId)
            .setParameter("accommodationId", accommodationId)
            .executeUpdate();
    }

    @Override
    public boolean existsFavorite(Long userId, Long accommodationId) {
        Integer exists = queryFactory
            .selectOne()
            .from(favorite)
            .where(userIdEq(userId), accommodationIdEq(accommodationId))
            .fetchFirst();
        return exists != null;
    }

    @Override
    public Long findFavoriteId(Long userId, Long accommodationId) {
        return queryFactory
            .select(favorite.favoriteId)
            .from(favorite)
            .where(userIdEq(userId), accommodationIdEq(accommodationId))
            .fetchFirst();
    }

    @Override
    public List<FavoriteDto.ListItemDto> findFavoriteList(Long userId) {
        if (userId == null) {
            return List.of();
        }

        return queryFactory
            .select(Projections.constructor(
                FavoriteDto.ListItemDto.class,
                favorite.favoriteId,
                accommodation.accommodationId,
                accommodation.name,
                accommodation.address.addressGroup,
                accommodation.address.addressShort,
                accommodation.address.addressDetail,
                accommodation.cost,
                image.imageUrl.min()
            ))
            .from(favorite)
            .join(favorite.accommodation, accommodation)
            .leftJoin(accommodationImage).on(accommodationImage.accommodation.accommodationId.eq(accommodation.accommodationId))
            .leftJoin(accommodationImage.image, image)
            .where(userIdEq(userId))
            .groupBy(
                favorite.favoriteId,
                accommodation.accommodationId,
                accommodation.name,
                accommodation.address.addressGroup,
                accommodation.address.addressShort,
                accommodation.address.addressDetail,
                accommodation.cost
            )
            .orderBy(favorite.favoriteId.desc())
            .fetch();
    }

    private BooleanExpression userIdEq(Long userId) {
        return userId == null ? null : favorite.user.userId.eq(userId);
    }

    private BooleanExpression accommodationIdEq(Long accommodationId) {
        return accommodationId == null ? null : favorite.accommodation.accommodationId.eq(accommodationId);
    }
}
