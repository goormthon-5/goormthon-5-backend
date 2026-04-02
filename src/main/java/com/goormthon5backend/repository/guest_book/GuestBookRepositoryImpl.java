package com.goormthon5backend.repository.guest_book;

import com.goormthon5backend.domain.entity.QGuestBookImage;
import com.goormthon5backend.domain.entity.QGuestBook;
import com.goormthon5backend.domain.entity.QImage;
import com.goormthon5backend.domain.enums.GuestBookType;
import com.goormthon5backend.dto.guest_book.GuestBookDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class GuestBookRepositoryImpl implements GuestBookRepositoryCustom {

    private static final QGuestBook guestBook = QGuestBook.guestBook;
    private static final QGuestBookImage guestbookImage = QGuestBookImage.guestBookImage;
    private static final QImage image = QImage.image;

    private final EntityManager entityManager;
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Object[]> findRatingSummaryByAccommodationIds(List<Long> accommodationIds) {
        if (accommodationIds == null || accommodationIds.isEmpty()) {
            return List.of();
        }

        return queryFactory
            .select(
                guestBook.accommodation.accommodationId,
                guestBook.rating.avg(),
                guestBook.guestBookId.count()
            )
            .from(guestBook)
            .where(guestBook.accommodation.accommodationId.in(accommodationIds))
            .groupBy(guestBook.accommodation.accommodationId)
            .fetch()
            .stream()
            .map(tuple -> new Object[] {
                tuple.get(guestBook.accommodation.accommodationId),
                tuple.get(guestBook.rating.avg()),
                tuple.get(guestBook.guestBookId.count())
            })
            .toList();
    }

    @Override
    public List<Object[]> findRatingSummaryByAccommodationId(Long accommodationId) {
        if (accommodationId == null) {
            return List.of();
        }

        Object[] row = queryFactory
            .select(
                guestBook.rating.avg(),
                guestBook.guestBookId.count()
            )
            .from(guestBook)
            .where(guestBook.accommodation.accommodationId.eq(accommodationId))
            .fetch()
            .stream()
            .findFirst()
            .map(tuple -> new Object[] {
                tuple.get(guestBook.rating.avg()),
                tuple.get(guestBook.guestBookId.count())
            })
            .orElse(new Object[] {null, 0L});

        return Collections.singletonList(row);
    }

    @Override
    public List<GuestBookDto.ListItemDto> findListByAccommodationId(Long accommodationId) {
        if (accommodationId == null) {
            return List.of();
        }

        return queryFactory
            .select(Projections.constructor(
                GuestBookDto.ListItemDto.class,
                guestBook.guestBookId,
                guestBook.user.userId,
                guestBook.user.name,
                guestBook.content,
                guestBook.type.stringValue(),
                guestBook.rating,
                image.imageUrl,
                guestBook.createdAt,
                guestBook.updatedAt
            ))
            .from(guestBook)
            .join(guestBook.user)
            .leftJoin(guestbookImage).on(
                guestbookImage.guestBook.guestBookId.eq(guestBook.guestBookId)
                    .and(guestBook.type.eq(GuestBookType.IMAGE))
            )
            .leftJoin(guestbookImage.image, image)
            .where(guestBook.accommodation.accommodationId.eq(accommodationId))
            .orderBy(guestBook.createdAt.desc(), guestBook.guestBookId.desc())
            .fetch();
    }

    @Override
    public Long createGuestBook(Long accommodationId, Long userId, String content, GuestBookType type, Integer rating) {
        entityManager.createNativeQuery(
            """
                INSERT INTO guest_book (content, type, rating, updated_at, created_at, accommodation_id, user_id)
                VALUES (:content, :type, :rating, NOW(), NOW(), :accommodationId, :userId)
                """
        )
            .setParameter("content", content)
            .setParameter("type", type.name())
            .setParameter("rating", rating)
            .setParameter("accommodationId", accommodationId)
            .setParameter("userId", userId)
            .executeUpdate();

        return ((Number) entityManager.createNativeQuery("SELECT LAST_INSERT_ID()").getSingleResult()).longValue();
    }

    @Override
    public List<String> findTextContentsByAccommodationId(Long accommodationId) {
        if (accommodationId == null) {
            return List.of();
        }

        return queryFactory
            .select(guestBook.content)
            .from(guestBook)
            .where(
                guestBook.accommodation.accommodationId.eq(accommodationId),
                guestBook.type.eq(GuestBookType.TEXT)
            )
            .orderBy(guestBook.createdAt.desc(), guestBook.guestBookId.desc())
            .fetch();
    }
}
