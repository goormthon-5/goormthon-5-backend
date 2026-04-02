package com.goormthon5backend.repository.guest_book;

import com.goormthon5backend.domain.entity.GuestBook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuestBookRepository extends JpaRepository<GuestBook, Long>, GuestBookRepositoryCustom {
}
