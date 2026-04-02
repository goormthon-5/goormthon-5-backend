package com.goormthon5backend.repository.guest_book;

import com.goormthon5backend.domain.entity.GuestbookImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuestbookImageRepository extends JpaRepository<GuestbookImage, Long> {
}
