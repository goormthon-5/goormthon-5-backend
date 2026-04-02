package com.goormthon5backend.repository.guest_book;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class GuestbookImageRepositoryImpl implements GuestbookImageRepositoryCustom {

    private final EntityManager entityManager;

    @Override
    public Long createImage(String imageUrl) {
        entityManager.createNativeQuery(
            """
                INSERT INTO images (image_url)
                VALUES (:imageUrl)
                """
        )
            .setParameter("imageUrl", imageUrl)
            .executeUpdate();

        return ((Number) entityManager.createNativeQuery("SELECT LAST_INSERT_ID()").getSingleResult()).longValue();
    }

    @Override
    public void createGuestbookImage(Long guestBookId, Long imageId) {
        entityManager.createNativeQuery(
            """
                INSERT INTO guestbook_images (image_id, guest_book_id)
                VALUES (:imageId, :guestBookId)
                """
        )
            .setParameter("imageId", imageId)
            .setParameter("guestBookId", guestBookId)
            .executeUpdate();
    }
}
