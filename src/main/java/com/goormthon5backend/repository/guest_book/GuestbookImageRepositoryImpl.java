package com.goormthon5backend.repository.guest_book;

import com.goormthon5backend.domain.entity.GuestBook;
import com.goormthon5backend.domain.entity.QGuestBookImage;
import com.goormthon5backend.domain.entity.QImage;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class GuestbookImageRepositoryImpl implements GuestbookImageRepositoryCustom {

    private final EntityManager entityManager;
    private final JPAQueryFactory queryFactory;

    private static final QImage image = QImage.image;
    private static final QGuestBookImage guestBookImage = QGuestBookImage.guestBookImage;

    @Override
    public Long createImage(String imageUrl) {
        queryFactory
            .insert(image)
            .set(image.imageUrl, imageUrl)
            .execute();

        return queryFactory
            .select(image.imageId.max())
            .from(image)
            .where(image.imageUrl.eq(imageUrl))
            .fetchOne();
    }

    @Override
    public void createGuestbookImage(Long guestBookId, Long imageId) {
        GuestBook guestBookRef = entityManager.getReference(GuestBook.class, guestBookId);
        queryFactory
            .insert(guestBookImage)
            .set(guestBookImage.imageId, imageId)
            .set(guestBookImage.guestBook, guestBookRef)
            .execute();
    }
}
