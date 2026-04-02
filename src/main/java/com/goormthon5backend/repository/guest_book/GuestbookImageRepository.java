package com.goormthon5backend.repository.guest_book;

import com.goormthon5backend.domain.entity.GuestBookImage;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GuestbookImageRepository extends JpaRepository<GuestBookImage, Long> {

    @Query(
        """
            SELECT gi.guestBook.guestBookId, gi.image.imageUrl
            FROM GuestBookImage gi
            WHERE gi.guestBook.guestBookId IN :guestBookIds
            ORDER BY gi.guestBook.guestBookId ASC, gi.image.imageId ASC
            """
    )
    List<Object[]> findImageUrlsByGuestBookIds(@Param("guestBookIds") List<Long> guestBookIds);
}
