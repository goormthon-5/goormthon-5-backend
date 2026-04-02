package com.goormthon5backend.repository.guest_book;

import com.goormthon5backend.domain.entity.QGuestBook;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class GuestBookRepositoryImpl implements GuestBookRepositoryCustom {

    private static final QGuestBook guestBook = QGuestBook.guestBook;

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
}
