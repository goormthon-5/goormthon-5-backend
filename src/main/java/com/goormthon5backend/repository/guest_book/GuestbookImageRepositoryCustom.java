package com.goormthon5backend.repository.guest_book;

public interface GuestbookImageRepositoryCustom {

    Long createImage(String imageUrl);

    void createGuestbookImage(Long guestBookId, Long imageId);
}
